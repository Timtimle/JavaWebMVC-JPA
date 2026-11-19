package vn.edu.hcmute.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmute.service.impl.OtpService;
import vn.edu.hcmute.util.FormValidation;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private final OtpService otp = new OtpService();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        vn.edu.hcmute.util.JspView.render(req, resp, "/WEB-INF/views/forgot-password.jsp");
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        email = email == null ? "" : email.trim();
        req.setAttribute("email", email);
        if (!FormValidation.email(email)) {
            req.setAttribute("alert", "Enter a valid email address."); doGet(req,resp); return;
        }
        try { otp.send(email, OtpService.RESET); }
        catch (MessagingException | IllegalStateException ex) {
            req.setAttribute("alert", "Unable to send OTP: " + ex.getMessage()); doGet(req,resp); return;
        }
        resp.sendRedirect(req.getContextPath() + "/reset-password?email=" +
                URLEncoder.encode(email, StandardCharsets.UTF_8));
    }
}
