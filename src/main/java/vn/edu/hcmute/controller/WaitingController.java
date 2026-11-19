package vn.edu.hcmute.controller;

import vn.edu.hcmute.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/waiting")
public class WaitingController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Handle requests after successful login
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        // Prevent caching of authenticated pages
        resp.setHeader("Cache-Control", "no-store");

        // Retrieve the current session
        HttpSession session = req.getSession(false);

        // Redirect unauthenticated users to login
        if (session == null ||
            !(session.getAttribute("account") instanceof User)) {

            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Retrieve authenticated user information
        User user = (User) session.getAttribute("account");

        req.setAttribute("user", user);

        // Display the temporary welcome page
        vn.edu.hcmute.util.JspView.render(req, resp, "/WEB-INF/views/waiting.jsp");
    }
}
