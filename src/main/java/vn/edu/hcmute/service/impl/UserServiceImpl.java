package vn.edu.hcmute.service.impl;

import vn.edu.hcmute.dao.UserDAO;
import vn.edu.hcmute.dao.impl.UserDAOImpl;
import vn.edu.hcmute.model.User;
import vn.edu.hcmute.service.UserService;

import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO = new UserDAOImpl();

    // Authenticate user credentials
    @Override
    public User login(String username, String password)
            throws SQLException {

        if (username == null || password == null ||
            username.isBlank() || password.isBlank()) {
            return null;
        }

        User user = userDAO.getByUsername(username.trim());

        if (user == null || user.getPassword() == null) {
            return null;
        }

        // Verify the password against the stored BCrypt hash
        try {
            if (BCrypt.checkpw(password, user.getPassword())) {
                // Do not expose the password hash in the session
                user.setPassword(null);
                return user;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }

        return null;
    }

    // Register a new user account
    @Override
    public boolean register(String username, String password,
                            String email, String fullName, String phone)
            throws SQLException {

        if (username == null || username.isBlank() ||
            password == null || password.length() < 8 ||
            email == null || email.isBlank()) {
            return false;
        }

        username = username.trim();
        email = email.trim();

        // Check for duplicate username or email
        if (checkExistUsername(username) || checkExistEmail(email)) {
            return false;
        }

        // Hash the password before storing it
        String hashedPassword = BCrypt.hashpw(
            password, BCrypt.gensalt(12)
        );

        // Create a new user object
        User user = new User();

        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        user.setFullName(fullName == null ? "" : fullName.trim());
        user.setPhone(phone == null ? "" : phone.trim());
        user.setAvatar(null);
        user.setRoleId(3);

        // Save the user to the database
        return userDAO.insert(user);
    }

    // Check if a username is already registered
    @Override
    public boolean checkExistUsername(String username)
            throws SQLException {

        return userDAO.existsByUsername(username);
    }

    // Check if an email is already registered
    @Override
    public boolean checkExistEmail(String email)
            throws SQLException {

        return userDAO.existsByEmail(email);
    }
}
