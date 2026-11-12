package vn.edu.hcmute.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet("/category/image")
public class CategoryImageController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Path UPLOAD_DIR =
            Path.of("E:\\App\\Tool\\javawebmvc-uploads");

    // Display an uploaded category image
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws IOException {

        // Require an authenticated session
        if (req.getSession(false) == null ||
            req.getSession(false).getAttribute("account") == null) {

            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String fileName = req.getParameter("name");

        // Validate the generated image filename
        if (fileName == null || !fileName.matches(
                "(?i)[0-9a-f]{8}-(?:[0-9a-f]{4}-){3}" +
                "[0-9a-f]{12}\\.(jpg|png|gif)")) {

            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Path file = UPLOAD_DIR.resolve(fileName);

        // Check whether the image exists
        if (!Files.isRegularFile(file)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Determine the response content type
        String lowerName = fileName.toLowerCase();

        if (lowerName.endsWith(".png")) {
            resp.setContentType("image/png");
        } else if (lowerName.endsWith(".gif")) {
            resp.setContentType("image/gif");
        } else {
            resp.setContentType("image/jpeg");
        }

        resp.setHeader("X-Content-Type-Options", "nosniff");

        // Stream the image to the browser
        try (OutputStream output = resp.getOutputStream()) {
            Files.copy(file, output);
        }
    }
}
