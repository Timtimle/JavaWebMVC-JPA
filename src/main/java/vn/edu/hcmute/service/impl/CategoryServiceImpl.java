package vn.edu.hcmute.service.impl;

import vn.edu.hcmute.dao.CategoryDAO;
import vn.edu.hcmute.dao.impl.CategoryDAOImpl;
import vn.edu.hcmute.model.Category;
import vn.edu.hcmute.service.CategoryService;

import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO categoryDAO =
            new CategoryDAOImpl();

    // Validate category name
    private boolean isValidCategory(Category category) {

        if (category == null || category.getName() == null) {
            return false;
        }

        String name = category.getName().trim();

        return !name.isEmpty() && name.length() <= 255;
    }

    // Create a new category
    @Override
    public boolean insert(Category category)
            throws SQLException {

        if (!isValidCategory(category)) {
            return false;
        }

        // Normalize category name
        category.setName(category.getName().trim());

        return categoryDAO.insert(category);
    }

    // Update an existing category
    @Override
    public boolean update(Category category)
            throws SQLException {

        if (!isValidCategory(category) ||
            category.getId() <= 0) {
            return false;
        }

        // Find the existing category
        Category existingCategory =
                categoryDAO.getById(category.getId());

        if (existingCategory == null) {
            return false;
        }

        // Update category name
        existingCategory.setName(
            category.getName().trim()
        );

        // Preserve the old icon when no new icon is provided
        if (category.getIcon() != null &&
            !category.getIcon().isBlank()) {

            existingCategory.setIcon(category.getIcon());
        }

        return categoryDAO.update(existingCategory);
    }

    // Delete a category by ID
    @Override
    public boolean delete(int id) throws SQLException {

        if (id <= 0) {
            return false;
        }

        return categoryDAO.delete(id);
    }

    // Retrieve category information by ID
    @Override
    public Category getById(int id) throws SQLException {

        if (id <= 0) {
            return null;
        }

        return categoryDAO.getById(id);
    }

    // Retrieve category information by name
    @Override
    public Category getByName(String name)
            throws SQLException {

        if (name == null || name.isBlank()) {
            return null;
        }

        return categoryDAO.getByName(name.trim());
    }

    // Retrieve all categories
    @Override
    public List<Category> getAll() throws SQLException {

        return categoryDAO.getAll();
    }

    // Search categories by keyword
    @Override
    public List<Category> search(String keyword)
            throws SQLException {

        if (keyword == null || keyword.isBlank()) {
            return categoryDAO.getAll();
        }

        return categoryDAO.search(keyword.trim());
    }
}
