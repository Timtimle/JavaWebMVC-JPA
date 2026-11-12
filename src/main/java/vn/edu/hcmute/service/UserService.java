package vn.edu.hcmute.service;

import vn.edu.hcmute.model.User;
import java.sql.SQLException;

public interface UserService {

    // Authenticate a user with username and password
    User login(String username, String password)
            throws SQLException;

    // Register a new user account
    boolean register(String username, String password,
                     String email, String fullName, String phone)
            throws SQLException;

    // Check if the username already exists
    boolean checkExistUsername(String username)
            throws SQLException;

    // Check if the email already exists
    boolean checkExistEmail(String email)
            throws SQLException;
}
