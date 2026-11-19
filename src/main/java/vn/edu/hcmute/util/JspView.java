package vn.edu.hcmute.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class JspView {
    private JspView() {}

    public static void render(HttpServletRequest request,
                              HttpServletResponse response,
                              String path) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        request.getRequestDispatcher(path).include(request, response);
    }
}
