package vn.edu.hcmute.dao;

import java.sql.SQLException;

public interface RememberTokenDAO {

    // Save a persistent login token
    boolean save(int userId, String tokenHash)
            throws SQLException;

    // Find a username using a valid token hash
    String findUsernameByTokenHash(String tokenHash)
            throws SQLException;

    // Revoke a persistent login token
    void deleteByTokenHash(String tokenHash)
            throws SQLException;
}
