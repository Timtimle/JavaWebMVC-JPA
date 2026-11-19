package vn.edu.hcmute.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.edu.hcmute.dao.VideoDAO;
import vn.edu.hcmute.util.FormValidation;
import vn.edu.hcmute.model.User;
import vn.edu.hcmute.model.Video;
import vn.edu.hcmute.service.impl.VideoStorage;

import java.io.IOException;

@WebServlet(urlPatterns = {"/video", "/video/new", "/video/edit", "/video/delete", "/video/watch"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 100L * 1024 * 1024,
        maxRequestSize = 105L * 1024 * 1024)
public class VideoController extends HttpServlet {
    private static final int PAGE_SIZE = 6;
    private static final String FORM = "/WEB-INF/views/video/form.jsp";

    private final VideoDAO dao = new VideoDAO();
    private final VideoStorage storage = new VideoStorage();

    private User account(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("account") instanceof User user ? user : null;
    }

    private long videoId(HttpServletRequest request) {
        try {
            return Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private int page(HttpServletRequest request) {
        try {
            return Math.max(1, Math.min(100000, Integer.parseInt(request.getParameter("page"))));
        } catch (NumberFormatException ex) {
            return 1;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        // Anyone can watch videos; only authenticated users can manage their own uploads.
        if ("/video/watch".equals(path)) {
            Video video = dao.find(videoId(request));
            if (video == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            dao.incrementViews(video.getId());
            request.setAttribute("video", video);
            vn.edu.hcmute.util.JspView.render(request, response, "/WEB-INF/views/video/watch.jsp");
            return;
        }

        User user = account(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        response.setHeader("Cache-Control", "no-store");
        switch (path) {
            case "/video" -> {
                long count = dao.countByOwner(user.getId());
                long pages = Math.max(1, (count + PAGE_SIZE - 1) / PAGE_SIZE);
                int current = (int) Math.min(page(request), pages);
                request.setAttribute("videos", dao.byOwner(user.getId(), current));
                request.setAttribute("page", current);
                request.setAttribute("pages", pages);
                request.setAttribute("total", count);
                vn.edu.hcmute.util.JspView.render(request, response, "/WEB-INF/views/video/list.jsp");
            }
            case "/video/new" -> {
                prepareForm(request, null);
                vn.edu.hcmute.util.JspView.render(request, response, FORM);
            }
            case "/video/edit" -> {
                Video video = dao.find(videoId(request));
                if (!ownedBy(video, user)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                prepareForm(request, video);
                vn.edu.hcmute.util.JspView.render(request, response, FORM);
            }
            default -> response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = account(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        long id = videoId(request);

        if ("/video/delete".equals(path)) {
            Video existing = dao.find(id);
            if (!ownedBy(existing, user)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (dao.delete(id, user.getId())) {
                storage.delete(existing.getFileName());
            }
            response.sendRedirect(request.getContextPath() + "/video?deleted=1");
            return;
        }

        boolean editing = "/video/edit".equals(path);
        if (!editing && !"/video/new".equals(path)) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        Video existing = editing ? dao.find(id) : null;
        if (editing && !ownedBy(existing, user)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String uploaded = null;
        try {
            String title = trim(request.getParameter("title"));
            String description = trim(request.getParameter("description"));
            request.setAttribute("formTitle", title);
            request.setAttribute("formDescription", description);
            if (!FormValidation.videoTitle(title) || description.length() > 2000) {
                showForm(request, response, existing, "Title is required (max 255 characters); description max 2000.");
                return;
            }
            Part file = request.getPart("videoFile");
            if (file != null && file.getSize() > 0) {
                uploaded = storage.save(file);
            }
            if (!editing && uploaded == null) {
                showForm(request, response, null, "Choose a video file to upload.");
                return;
            }

            if (editing) {
                if (!dao.update(id, user.getId(), title, description, uploaded)) {
                    if (uploaded != null) storage.delete(uploaded);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                // Remove the old file only after the database transaction succeeds.
                if (uploaded != null) storage.delete(existing.getFileName());
            } else {
                Video video = new Video();
                video.setTitle(title);
                video.setDescription(description);
                video.setFileName(uploaded);
                dao.insert(video, user.getId());
            }
            response.sendRedirect(request.getContextPath() + "/video?saved=1");
        } catch (IllegalStateException ex) {
            if (uploaded != null) storage.delete(uploaded);
            log("Video upload rejected by multipart limits", ex);
            response.sendError(413, "Video must be 100 MB or smaller.");
        } catch (IOException ex) {
            if (uploaded != null) storage.delete(uploaded);
            log("Video upload failed", ex);
            showForm(request, response, existing, ex.getMessage());
        } catch (ServletException | RuntimeException ex) {
            if (uploaded != null) storage.delete(uploaded);
            log("Video save failed", ex);
            showForm(request, response, existing, "Unable to save video. Check the file and try again.");
        }
    }

    private static boolean ownedBy(Video video, User user) {
        return video != null && video.getOwner().getId() == user.getId();
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private void prepareForm(HttpServletRequest request, Video video) {
        request.setAttribute("video", video);
        request.setAttribute("formTitle", video == null ? "" : video.getTitle());
        request.setAttribute("formDescription", video == null ? "" : video.getDescription());
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response,
                          Video video, String error) throws ServletException, IOException {
        request.setAttribute("video", video);
        request.setAttribute("alert", error);
        if (request.getAttribute("formTitle") == null) {
            request.setAttribute("formTitle", video == null ? "" : video.getTitle());
        }
        if (request.getAttribute("formDescription") == null) {
            request.setAttribute("formDescription", video == null ? "" : video.getDescription());
        }
        vn.edu.hcmute.util.JspView.render(request, response, FORM);
    }
}
