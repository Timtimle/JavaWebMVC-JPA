package vn.edu.hcmute.controller;

import vn.edu.hcmute.model.Category;
import vn.edu.hcmute.service.CategoryService;
import vn.edu.hcmute.service.impl.CategoryServiceImpl;

import vn.edu.hcmute.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet("/category/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 5 * 1024 * 1024,
    maxRequestSize = 6 * 1024 * 1024
)
public class CategoryController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final CategoryService categoryService =
            new CategoryServiceImpl();

    private static final Path UPLOAD_DIR =
            Path.of("E:\\App\\Tool\\javawebmvc-uploads");

    private static final String VIEW_DIR =
            "/WEB-INF/views/category/";

    // Handle category pages and search requests
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        if (!isAuthenticated(req, resp)) {
            return;
        }

        String action = req.getPathInfo();

        try {
            if (action == null || action.equals("/")) {
                resp.sendRedirect(
                    req.getContextPath() + "/category/list"
                );
                return;
            }

            switch (action) {

                case "/list" -> {
                    String keyword = req.getParameter("keyword");

                    // Retrieve categories or search by keyword
                    List<Category> categories =
                            categoryService.search(keyword);

                    req.setAttribute("cateList", categories);
                    req.setAttribute("keyword", keyword);

                    req.getRequestDispatcher(
                        VIEW_DIR + "list-category.jsp"
                    ).forward(req, resp);
                }

                case "/add" -> {
                    // Display the category creation form
                    req.getRequestDispatcher(
                        VIEW_DIR + "add-category.jsp"
                    ).forward(req, resp);
                }

                case "/edit" -> {
                    // Find the category to edit
                    int id = parseId(req.getParameter("id"));

                    Category category =
                            categoryService.getById(id);

                    if (category == null) {
                        resp.sendError(404, "Category not found.");
                        return;
                    }

                    req.setAttribute("category", category);

                    req.getRequestDispatcher(
                        VIEW_DIR + "edit-category.jsp"
                    ).forward(req, resp);
                }

                default -> resp.sendError(404);
            }

        } catch (IllegalArgumentException e) {
            resp.sendError(400, "Invalid category ID.");

        } catch (SQLException e) {
            log("Failed to retrieve categories", e);
            throw new ServletException("Database error", e);
        }
    }

    // Handle category creation, updates, and deletion
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        if (!isAuthenticated(req, resp)) {
            return;
        }

        req.setCharacterEncoding("UTF-8");

        String action = req.getPathInfo();

        try {
            if (action == null) {
                resp.sendError(404);
                return;
            }

            switch (action) {

                case "/add" -> addCategory(req, resp);

                case "/edit" -> updateCategory(req, resp);

                case "/delete" -> deleteCategory(req, resp);

                default -> resp.sendError(404);
            }

        } catch (IllegalArgumentException e) {
            resp.sendError(400, e.getMessage());

        } catch (SQLException e) {
            log("Category database operation failed", e);
            throw new ServletException("Database error", e);
        }
    }

    // Create a new category
    private void addCategory(HttpServletRequest req,
                             HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        Category category = new Category();

        category.setName(req.getParameter("catename"));

        // Save the uploaded image if one is provided
        String imageName = saveImage(req);
        category.setIcon(imageName);

        boolean saved = false;

        try {
            saved = categoryService.insert(category);

            if (!saved) {
                resp.sendError(400, "Invalid category name.");
                return;
            }

            redirectToList(req, resp);

        } finally {
            // Remove the uploaded file if database insertion fails
            if (!saved) {
                deleteUploadedImage(imageName);
            }
        }
    }

    // Update an existing category
    private void updateCategory(HttpServletRequest req,
                                HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        int id = parseId(req.getParameter("id"));

        Category existing = categoryService.getById(id);

        if (existing == null) {
            resp.sendError(404, "Category not found.");
            return;
        }

        Category category = new Category();

        category.setId(id);
        category.setName(req.getParameter("catename"));

        // Preserve the existing image if no new image is uploaded
        String imageName = saveImage(req);
        category.setIcon(imageName);

        boolean updated = false;

        try {
            updated = categoryService.update(category);

            if (!updated) {
                resp.sendError(400, "Invalid category data.");
                return;
            }

            redirectToList(req, resp);

        } finally {
            // Remove the new image if database update fails
            if (!updated) {
                deleteUploadedImage(imageName);
            }
        }
    }

    // Delete a category by ID
    private void deleteCategory(HttpServletRequest req,
                                HttpServletResponse resp)
            throws SQLException, IOException {

        int id = parseId(req.getParameter("id"));

        boolean deleted = categoryService.delete(id);

        if (!deleted) {
            resp.sendError(404, "Category not found.");
            return;
        }

        redirectToList(req, resp);
    }

    // Validate and store an uploaded category image
    private String saveImage(HttpServletRequest req)
            throws IOException, ServletException {

        Part part = req.getPart("icon");

        if (part == null || part.getSize() == 0) {
            return null;
        }

        String contentType = part.getContentType();
        String extension;

        // Allow supported image formats only
        if ("image/jpeg".equals(contentType)) {
            extension = ".jpg";
        } else if ("image/png".equals(contentType)) {
            extension = ".png";
        } else if ("image/gif".equals(contentType)) {
            extension = ".gif";
        } else {
            throw new IllegalArgumentException(
                "Only JPG, PNG, and GIF images are allowed."
            );
        }

        // Verify that the uploaded file is a readable image
        try (InputStream input = part.getInputStream()) {
            if (ImageIO.read(input) == null) {
                throw new IllegalArgumentException(
                    "Invalid image file."
                );
            }
        }

        // Generate a unique filename
        String fileName =
                UUID.randomUUID().toString() + extension;

        Files.createDirectories(UPLOAD_DIR);

        Path destination = UPLOAD_DIR.resolve(fileName);

        try (InputStream input = part.getInputStream()) {
            Files.copy(
                input,
                destination,
                StandardCopyOption.REPLACE_EXISTING
            );
        }

        return fileName;
    }

    // Remove a newly uploaded file after a failed operation
    private void deleteUploadedImage(String fileName) {

        if (fileName == null) {
            return;
        }

        try {
            Files.deleteIfExists(UPLOAD_DIR.resolve(fileName));
        } catch (IOException e) {
            log("Failed to clean up uploaded image", e);
        }
    }

    // Parse and validate a category ID
    private int parseId(String value) {

        try {
            int id = Integer.parseInt(value);

            if (id > 0) {
                return id;
            }
        } catch (NumberFormatException e) {
            // Invalid IDs are rejected below
        }

        throw new IllegalArgumentException(
            "Invalid category ID."
        );
    }

    // Require login for category management
    private boolean isAuthenticated(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws IOException {

        var session = req.getSession(false);

        // Redirect unauthenticated users to the login page
        if (session == null ||
            !(session.getAttribute("account") instanceof User)) {

            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }

        return true;
    }

    // Redirect to the category list
    private void redirectToList(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws IOException {

        resp.sendRedirect(
            req.getContextPath() + "/category/list"
        );
    }
}
