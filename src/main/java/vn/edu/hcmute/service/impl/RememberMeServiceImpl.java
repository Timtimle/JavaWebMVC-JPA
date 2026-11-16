package vn.edu.hcmute.service.impl;

import vn.edu.hcmute.dao.RememberTokenDAO;
import vn.edu.hcmute.dao.UserDAO;
import vn.edu.hcmute.dao.impl.RememberTokenDAOImpl;
import vn.edu.hcmute.dao.impl.UserDAOImpl;
import vn.edu.hcmute.model.User;
import vn.edu.hcmute.service.RememberMeService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HexFormat;

public class RememberMeServiceImpl implements RememberMeService {

    private final RememberTokenDAO tokenDAO =
            new RememberTokenDAOImpl();

    private final UserDAO userDAO =
            new UserDAOImpl();

    private static final SecureRandom SECURE_RANDOM =
            new SecureRandom();

    // Generate a cryptographically secure random token
    private String generateToken() {

        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    // Hash a token using SHA-256
    private String hashToken(String token) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 is not available", e
            );
        }
    }

    // Validate the expected token format
    private boolean isValidToken(String token) {
        return token != null &&
                token.matches("[A-Za-z0-9_-]{43}");
    }

    // Create and save a new persistent login token
    @Override
    public String createToken(int userId) throws SQLException {

        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        String token = generateToken();
        String tokenHash = hashToken(token);

        if (tokenDAO.save(userId, tokenHash)) {
            return token;
        }

        throw new SQLException("Failed to save remember-me token");
    }

    // Authenticate the user using a valid stored token
    @Override
    public User authenticate(String token) throws SQLException {

        if (!isValidToken(token)) {
            return null;
        }

        String tokenHash = hashToken(token);

        String username =
                tokenDAO.findUsernameByTokenHash(tokenHash);

        if (username == null) {
            return null;
        }

        User user = userDAO.getByUsername(username);

        if (user != null && !user.isActive()) return null;
        if (user != null) {
            // Remove the password hash from the session user
            user.setPassword(null);
        }

        return user;
    }

    // Revoke the token when the user logs out
    @Override
    public void revokeToken(String token) throws SQLException {

        if (!isValidToken(token)) {
            return;
        }

        tokenDAO.deleteByTokenHash(hashToken(token));
    }
}
