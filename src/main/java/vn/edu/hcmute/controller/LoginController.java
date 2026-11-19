package vn.edu.hcmute.controller;

import vn.edu.hcmute.model.User;
import vn.edu.hcmute.util.FormValidation;
import vn.edu.hcmute.service.UserService;
import vn.edu.hcmute.service.RememberMeService;
import vn.edu.hcmute.service.impl.UserServiceImpl;
import vn.edu.hcmute.service.impl.RememberMeServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserServiceImpl();
    private final RememberMeService rememberMeService =
            new RememberMeServiceImpl();

    private static final String LOGIN_VIEW =
            "/WEB-INF/views/login.jsp";

    private static final String TOKEN_COOKIE = "rememberToken";
    private static final String USERNAME_COOKIE = "rememberedUsername";

    private static final int COOKIE_AGE = 30 * 24 * 60 * 60;

    // Display the login page or restore a remembered session
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Check whether the user is already authenticated
        if (session != null &&
            session.getAttribute("account") != null) {

            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        // Authenticate using the persistent login token
        String token = getCookieValue(req, TOKEN_COOKIE);

        if (token != null) {
            try {
                User user = rememberMeService.authenticate(token);

                if (user != null) {
                    createSession(req, user);

                    resp.sendRedirect(
                        req.getContextPath() + "/waiting"
                    );
                    return;
                }

                // Remove invalid or expired cookies
                deleteCookie(req, resp, TOKEN_COOKIE);

            } catch (SQLException e) {
                log("Remember-me authentication error", e);
                req.setAttribute("alert",
                    "Database error. Please log in manually.");
            }
        }

        // Restore the remembered username
        String savedUsername =
                getCookieValue(req, USERNAME_COOKIE);

        if (savedUsername != null) {
            try {
                req.setAttribute(
                    "rememberedUsername",
                    URLDecoder.decode(
                        savedUsername,
                        StandardCharsets.UTF_8
                    )
                );
            } catch (IllegalArgumentException e) {
                deleteCookie(req, resp, USERNAME_COOKIE);
            }
        }

        vn.edu.hcmute.util.JspView.render(req, resp, LOGIN_VIEW);
    }

    // Process the login form
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        boolean rememberMe =
                "on".equals(req.getParameter("remember"));

        // Validate required fields
        if (username == null || username.isBlank() ||
            password == null || password.isBlank()) {

            showError(req, resp,
                "Username and password are required.");
            return;
        }

        username = username.trim();
        if (username.length() > 100 || password.length() > 128) {
            showError(req, resp, "Invalid username or password length.");
            return;
        }

        try {
            // Authenticate with username and password
            User user = userService.login(username, password);

            if (user == null) {
                req.setAttribute("rememberedUsername", username);
                showError(req, resp,
                    "Invalid credentials or account not activated. Check your email OTP.");
                return;
            }

            // Revoke the previous persistent token
            String oldToken = getCookieValue(req, TOKEN_COOKIE);

            if (oldToken != null) {
                rememberMeService.revokeToken(oldToken);
            }

            if (rememberMe) {

                // Generate and store a secure persistent token
                String token =
                        rememberMeService.createToken(user.getId());

                addCookie(req, resp,
                    TOKEN_COOKIE, token, COOKIE_AGE);

                // Save the username for the login form
                String encodedUsername = URLEncoder.encode(
                    user.getUsername(),
                    StandardCharsets.UTF_8
                );

                addCookie(req, resp,
                    USERNAME_COOKIE,
                    encodedUsername,
                    COOKIE_AGE);

            } else {

                // Remove existing persistent login cookies
                deleteCookie(req, resp, TOKEN_COOKIE);
                deleteCookie(req, resp, USERNAME_COOKIE);
            }

            // Create an authenticated session
            createSession(req, user);

            resp.sendRedirect(
                req.getContextPath() + "/waiting"
            );

        } catch (SQLException e) {
            log("Login database error", e);

            showError(req, resp,
                "Database error. Please try again.");
        }
    }

    // Create a session and regenerate its identifier
    private void createSession(HttpServletRequest req, User user) {

        HttpSession session = req.getSession(true);
        req.changeSessionId();

        session.setAttribute("account", user);
        session.setMaxInactiveInterval(30 * 60);
    }

    // Retrieve a cookie by name
    private String getCookieValue(
            HttpServletRequest req, String name) {

        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    // Add a cookie with security attributes
    private void addCookie(
            HttpServletRequest req,
            HttpServletResponse resp,
            String name,
            String value,
            int maxAge) {

        Cookie cookie = new Cookie(name, value);

        String path = req.getContextPath();
        cookie.setPath(path.isEmpty() ? "/" : path);

        cookie.setHttpOnly(true);
        cookie.setSecure(req.isSecure());
        cookie.setAttribute("SameSite", "Lax");
        cookie.setMaxAge(maxAge);

        resp.addCookie(cookie);
    }

    // Delete a cookie from the browser
    private void deleteCookie(
            HttpServletRequest req,
            HttpServletResponse resp,
            String name) {

        addCookie(req, resp, name, "", 0);
    }

    // Display a login error
    private void showError(
            HttpServletRequest req,
            HttpServletResponse resp,
            String message)
            throws ServletException, IOException {

        req.setAttribute("alert", message);
        vn.edu.hcmute.util.JspView.render(req, resp, LOGIN_VIEW);
    }
}
