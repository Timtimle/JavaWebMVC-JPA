package vn.edu.hcmute.controller;

import vn.edu.hcmute.service.UserService;
import vn.edu.hcmute.service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserServiceImpl();

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

        req.getRequestDispatcher(REGISTER_VIEW).forward(req, resp);
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

        // Validate required fields
        if (username.isEmpty() ||
            password == null || password.isEmpty() ||
            email.isEmpty()) {

            showError(req, resp, "Please fill in all required fields.");
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

                // Redirect to the login page
                resp.sendRedirect(
                    req.getContextPath() + "/login?registered=1"
                );

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
        req.getRequestDispatcher(REGISTER_VIEW).forward(req, resp);
    }
}
