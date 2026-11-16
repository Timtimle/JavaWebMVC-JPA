package vn.edu.hcmute.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import vn.edu.hcmute.model.*;
import vn.edu.hcmute.dao.VideoDAO;
import vn.edu.hcmute.service.impl.VideoStorage;
import java.io.*;

@WebServlet(urlPatterns={"/video","/video/new","/video/edit","/video/delete","/video/watch"})
@MultipartConfig(fileSizeThreshold=1024*1024,maxFileSize=100L*1024*1024,maxRequestSize=105L*1024*1024)
public class VideoController extends HttpServlet {
    private final VideoDAO dao=new VideoDAO();
    private final VideoStorage storage=new VideoStorage();

    private User account(HttpServletRequest req) {
        HttpSession s=req.getSession(false);
        return s!=null && s.getAttribute("account") instanceof User u ? u : null;
    }
    private long id(HttpServletRequest req) {
        try {return Long.parseLong(req.getParameter("id"));}catch(Exception ex){return -1;}
    }
    private int page(HttpServletRequest req) {
        try {return Math.max(1,Math.min(100000,Integer.parseInt(req.getParameter("page"))));}
        catch(Exception ex){return 1;}
    }
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {
        String path=req.getServletPath();
        User user=account(req);
        if("/video/watch".equals(path)) {
            Video video=dao.find(id(req));
            if(video==null){resp.sendError(404);return;}
            dao.incrementViews(video.getId());
            req.setAttribute("video",video);
            req.getRequestDispatcher("/WEB-INF/views/video/watch.jsp").forward(req,resp);return;
        }
        if(user==null){resp.sendRedirect(req.getContextPath()+"/login");return;}
        if("/video".equals(path)) {
            int current=page(req);
            long total=dao.countByOwner(user.getId());
            long pages=Math.max(1,(total+5)/6);
            current=(int)Math.min(current,pages);
            req.setAttribute("videos",dao.byOwner(user.getId(),current));
            req.setAttribute("page",current);req.setAttribute("pages",pages);
            req.getRequestDispatcher("/WEB-INF/views/video/list.jsp").forward(req,resp);return;
        }
        if("/video/new".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/video/form.jsp").forward(req,resp);return;
        }
        if("/video/edit".equals(path)) {
            Video video=dao.find(id(req));
            if(video==null||video.getOwner().getId()!=user.getId()){resp.sendError(404);return;}
            req.setAttribute("video",video);
            req.getRequestDispatcher("/WEB-INF/views/video/form.jsp").forward(req,resp);return;
        }
        resp.sendError(405);
    }
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException {
        User user=account(req);
        if(user==null){resp.sendRedirect(req.getContextPath()+"/login");return;}
        req.setCharacterEncoding("UTF-8");
        String path=req.getServletPath();
        long id=id(req);
        if("/video/delete".equals(path)) {
            Video prior=dao.find(id);
            if(prior==null||prior.getOwner().getId()!=user.getId()) {resp.sendError(404);return;}
            if(dao.delete(id,user.getId()))storage.delete(prior.getFileName());
            resp.sendRedirect(req.getContextPath()+"/video");return;
        }
        boolean edit="/video/edit".equals(path);
        if(!edit&&!"/video/new".equals(path)){resp.sendError(405);return;}
        Video prior=edit?dao.find(id):null;
        if(edit&&(prior==null||prior.getOwner().getId()!=user.getId())){
            resp.sendError(404);return;
        }
        String title=req.getParameter("title");
        String description=req.getParameter("description");
        title=title==null?"":title.trim();
        description=description==null?"":description.trim();
        if(title.isEmpty()||title.length()>255||description.length()>2000){
            showForm(req,resp,prior,"Title is required (max 255 characters); description max 2000.");return;
        }
        Part part=req.getPart("videoFile");
        String saved=null;
        try {
            if(part!=null&&part.getSize()>0)saved=storage.save(part);
            if(!edit&&saved==null) {
                showForm(req,resp,null,"Please select a video file.");return;
            }
            if(edit) {
                if(!dao.update(id,user.getId(),title,description,saved)){
                    resp.sendError(404);return;
                }
                if(saved!=null)storage.delete(prior.getFileName());
            } else {
                Video video=new Video();
                video.setTitle(title);video.setDescription(description);video.setFileName(saved);
                dao.insert(video,user.getId());
            }
            resp.sendRedirect(req.getContextPath()+"/video");
        } catch(IOException|RuntimeException ex) {
            if(saved!=null)storage.delete(saved);
            log("Video save failed",ex);
            showForm(req,resp,prior,"Could not save video: " + ex.getMessage());
        }
    }
    private void showForm(HttpServletRequest req,HttpServletResponse resp,Video video,String error)
            throws ServletException,IOException {
        req.setAttribute("video",video);req.setAttribute("alert",error);
        req.getRequestDispatcher("/WEB-INF/views/video/form.jsp").forward(req,resp);
    }
}
