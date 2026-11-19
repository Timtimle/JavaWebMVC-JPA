package vn.edu.hcmute.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmute.model.User;
import vn.edu.hcmute.service.impl.CloudinaryService;
import vn.edu.hcmute.service.impl.ProfileService;
import vn.edu.hcmute.util.FormValidation;

import java.io.IOException;

@WebServlet(urlPatterns = {"/profile", "/profile/verify-email"})
@MultipartConfig(maxFileSize = 5L * 1024 * 1024, maxRequestSize = 6L * 1024 * 1024)
public class ProfileController extends HttpServlet {
    private final ProfileService profiles = new ProfileService();
    private final CloudinaryService cloudinary = new CloudinaryService();
    private static final String VIEW = "/WEB-INF/views/profile.jsp";

    private User account(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("account") instanceof User user ? user : null;
    }
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setHeader("Cache-Control", "no-store");
        User current = account(req);
        if (current == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        show(req, resp, null, null);
    }
    private void show(HttpServletRequest req, HttpServletResponse resp, String error, String info)
            throws ServletException, IOException {
        User current = account(req);
        User fresh = profiles.find(current.getId());
        if (fresh == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        fresh.setPassword(null);
        req.getSession().setAttribute("account", fresh);
        req.setAttribute("user", fresh);
        req.setAttribute("pendingEmail", profiles.pendingEmail(fresh.getId()));
        req.setAttribute("alert", error);
        req.setAttribute("info", info);
        vn.edu.hcmute.util.JspView.render(req, resp, VIEW);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User current = account(req);
        if (current == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        req.setCharacterEncoding("UTF-8");
        if ("/profile/verify-email".equals(req.getServletPath())) {
            try {
                boolean ok = profiles.confirmEmailChange(current.getId(), req.getParameter("code"));
                show(req, resp, ok ? null : "Invalid or expired code.", ok ? "Email updated." : null);
            } catch (RuntimeException ex) {
                log("Email verification failed", ex);
                show(req, resp, "Could not update email.", null);
            }
            return;
        }
        String fullName = value(req.getParameter("fullname"));
        String phone = value(req.getParameter("phone"));
        String newEmail = value(req.getParameter("email"));
        if (fullName.isBlank() || !FormValidation.fullName(fullName) || !FormValidation.phone(phone)
                || !FormValidation.email(newEmail)) {
            show(req, resp, "Please check your name, phone and email.", null); return;
        }
        try {
            Part image = req.getPart("image");
            String avatar = cloudinary.uploadAvatar(image);
            profiles.update(current.getId(), fullName, phone, avatar);
            User latest = profiles.find(current.getId());
            if (!newEmail.equalsIgnoreCase(latest.getEmail())) {
                profiles.requestEmailChange(current.getId(), newEmail);
                show(req, resp, null, "Profile saved. Check your new email for the verification code.");
            } else {
                show(req, resp, null, "Profile updated.");
            }
        } catch (MessagingException | IOException | IllegalArgumentException | IllegalStateException ex) {
            show(req, resp, ex.getMessage(), null);
        } catch (RuntimeException ex) {
            log("Profile update failed", ex);
            show(req, resp, "Could not save your profile.", null);
        }
    }
    private String value(String value) { return value == null ? "" : value.trim(); }
}
