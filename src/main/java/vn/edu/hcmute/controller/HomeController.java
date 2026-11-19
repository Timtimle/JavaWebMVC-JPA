package vn.edu.hcmute.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmute.dao.VideoDAO;
import java.io.IOException;

@WebServlet("/home")
public class HomeController extends HttpServlet {
    private final VideoDAO videos = new VideoDAO();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("videos",videos.latestTen());
        vn.edu.hcmute.util.JspView.render(req, resp, "/WEB-INF/views/video/home.jsp");
    }
}
