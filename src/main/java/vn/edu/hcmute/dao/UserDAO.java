package vn.edu.hcmute.dao;

import vn.edu.hcmute.model.User;
import java.sql.SQLException;

public interface UserDAO {

    // Find a user by username
    User getByUsername(String username) throws SQLException;

    // Check if a username already exists
    boolean existsByUsername(String username) throws SQLException;

    // Check if an email already exists
    boolean existsByEmail(String email) throws SQLException;

    // Insert a new user into the database
    boolean insert(User user) throws SQLException;
}
