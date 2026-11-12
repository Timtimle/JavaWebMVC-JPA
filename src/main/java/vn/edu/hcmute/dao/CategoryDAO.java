package vn.edu.hcmute.dao;

import vn.edu.hcmute.model.Category;
import java.sql.SQLException;
import java.util.List;

public interface CategoryDAO {

    // Insert a new category
    boolean insert(Category category) throws SQLException;

    // Update an existing category
    boolean update(Category category) throws SQLException;

    // Delete a category by ID
    boolean delete(int id) throws SQLException;

    // Find a category by ID
    Category getById(int id) throws SQLException;

    // Find a category by name
    Category getByName(String name) throws SQLException;

    // Retrieve all categories
    List<Category> getAll() throws SQLException;

    // Search categories by keyword
    List<Category> search(String keyword) throws SQLException;
}
