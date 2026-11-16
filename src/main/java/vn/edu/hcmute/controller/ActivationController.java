package vn.edu.hcmute.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmute.service.impl.OtpService;
import java.io.IOException;

@WebServlet("/activate")
public class ActivationController extends HttpServlet {
    private final OtpService otp = new OtpService();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/activate.jsp").forward(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String action = req.getParameter("action");
        req.setAttribute("email", email);
        if ("resend".equals(action)) {
            try { otp.send(email, OtpService.ACTIVATE); req.setAttribute("info", "If eligible, a code was sent."); }
            catch (MessagingException | IllegalStateException e) {
                req.setAttribute("alert", "Could not send the code: " + e.getMessage());
            }
        } else if (otp.verify(email, OtpService.ACTIVATE, req.getParameter("code"), null)) {
            resp.sendRedirect(req.getContextPath() + "/login?activated=1"); return;
        } else req.setAttribute("alert", "Invalid or expired verification code.");
        doGet(req, resp);
    }
}
