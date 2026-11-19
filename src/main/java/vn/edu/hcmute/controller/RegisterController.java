package vn.edu.hcmute.controller;

import vn.edu.hcmute.service.UserService;
import vn.edu.hcmute.util.FormValidation;
import vn.edu.hcmute.service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.mail.MessagingException;
import vn.edu.hcmute.service.impl.OtpService;

@WebServlet("/register")
public class RegisterController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserServiceImpl();
    private final OtpService otpService = new OtpService();

    private static final String REGISTER_VIEW =
            "/WEB-INF/views/register.jsp";

    // Display the registration page
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Redirect users who are already logged in
        if (session != null &&
            session.getAttribute("account") != null) {

            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        vn.edu.hcmute.util.JspView.render(req, resp, REGISTER_VIEW);
    }

    // Process the registration form
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String fullName = req.getParameter("fullname");
        String phone = req.getParameter("phone");

        // Normalize user input
        username = username == null ? "" : username.trim();
        email = email == null ? "" : email.trim();
        fullName = fullName == null ? "" : fullName.trim();
        phone = phone == null ? "" : phone.trim();

        // Preserve non-sensitive form values
        req.setAttribute("username", username);
        req.setAttribute("email", email);
        req.setAttribute("fullname", fullName);
        req.setAttribute("phone", phone);

        if (!FormValidation.username(username)) {
            showError(req, resp, "Username must be 3-100 characters (letters, numbers, _ . -).");
            return;
        }
        if (!FormValidation.password(password)) {
            showError(req, resp, "Password must be 8-128 characters.");
            return;
        }
        if (!FormValidation.email(email) || !FormValidation.fullName(fullName) ||
                !FormValidation.phone(phone)) {
            showError(req, resp, "Please check email, name and phone number.");
            return;
        }

        // Validate input lengths
        if (username.length() > 100 ||
            email.length() > 255 ||
            fullName.length() > 255 ||
            phone.length() > 20) {

            showError(req, resp, "One or more fields are too long.");
            return;
        }

        // Validate password length
        if (password.length() < 8) {
            showError(req, resp,
                "Password must contain at least 8 characters.");
            return;
        }

        // Validate email format
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            showError(req, resp, "Invalid email address.");
            return;
        }

        try {

            // Check for duplicate usernames
            if (userService.checkExistUsername(username)) {
                showError(req, resp, "Username already exists.");
                return;
            }

            // Check for duplicate email addresses
            if (userService.checkExistEmail(email)) {
                showError(req, resp, "Email already exists.");
                return;
            }

            // Register the new user
            boolean success = userService.register(
                username,
                password,
                email,
                fullName,
                phone
            );

            if (success) {

                try {
                    otpService.send(email, OtpService.ACTIVATE);
                } catch (MessagingException | IllegalStateException ex) {
                    log("Activation OTP email was not sent", ex);
                    // The account is inactive and can request a new OTP later.
                }
                resp.sendRedirect(req.getContextPath() + "/activate?email=" +
                    java.net.URLEncoder.encode(email, java.nio.charset.StandardCharsets.UTF_8));

            } else {

                showError(req, resp,
                    "Registration failed. Please try again.");
            }

        } catch (SQLException e) {

            // Log the database error
            log("Registration database error", e);

            showError(req, resp,
                "Database error. Please try again.");
        }
    }

    // Forward an error message to the registration page
    private void showError(HttpServletRequest req,
                           HttpServletResponse resp,
                           String message)
            throws ServletException, IOException {

        req.setAttribute("alert", message);
        vn.edu.hcmute.util.JspView.render(req, resp, REGISTER_VIEW);
    }
}
