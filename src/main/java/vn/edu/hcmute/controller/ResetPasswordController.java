package vn.edu.hcmute.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmute.service.impl.OtpService;
import vn.edu.hcmute.util.FormValidation;
import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordController extends HttpServlet {
    private final OtpService otp = new OtpService();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        vn.edu.hcmute.util.JspView.render(req, resp, "/WEB-INF/views/reset-password.jsp");
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");
        if (!FormValidation.password(password) || !password.equals(confirm)) {
            req.setAttribute("alert", "Passwords must match and contain at least 8 characters."); doGet(req,resp); return;
        }
        if (otp.verify(email, OtpService.RESET, req.getParameter("code"), password)) {
            HttpSession old = req.getSession(false);
            if (old != null) old.invalidate();
            // Clear remembered credentials in this browser.
            for (String name : new String[]{"rememberToken", "rememberedUsername"}) {
                Cookie cookie = new Cookie(name, "");
                cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                cookie.setHttpOnly(true); cookie.setMaxAge(0); resp.addCookie(cookie);
            }
            resp.sendRedirect(req.getContextPath() + "/login?reset=1"); return;
        }
        req.setAttribute("alert", "Invalid or expired verification code."); doGet(req,resp);
    }
}
