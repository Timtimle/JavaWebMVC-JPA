package vn.edu.hcmute.controller;

import vn.edu.hcmute.service.RememberMeService;
import vn.edu.hcmute.service.impl.RememberMeServiceImpl;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final RememberMeService rememberMeService =
            new RememberMeServiceImpl();

    // Revoke persistent login and destroy the session
    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws IOException {

        String token = null;
        Cookie[] cookies = req.getCookies();

        // Find the persistent login token
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // Revoke the token on the server
        if (token != null) {
            try {
                rememberMeService.revokeToken(token);
            } catch (SQLException e) {
                log("Failed to revoke remember-me token", e);
                resp.sendError(
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Logout could not revoke the persistent token."
                );
                return;
            }
        }

        // Invalidate the current session
        HttpSession session = req.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        // Remove authentication and username cookies
        deleteCookie(req, resp, "rememberToken");
        deleteCookie(req, resp, "rememberedUsername");

        resp.setHeader("Cache-Control", "no-store");

        // Redirect to the login page
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    // Remove a cookie from the browser
    private void deleteCookie(
            HttpServletRequest req,
            HttpServletResponse resp,
            String name) {

        Cookie cookie = new Cookie(name, "");

        String path = req.getContextPath();
        cookie.setPath(path.isEmpty() ? "/" : path);

        cookie.setHttpOnly(true);
        cookie.setSecure(req.isSecure());
        cookie.setAttribute("SameSite", "Lax");
        cookie.setMaxAge(0);

        resp.addCookie(cookie);
    }
}
