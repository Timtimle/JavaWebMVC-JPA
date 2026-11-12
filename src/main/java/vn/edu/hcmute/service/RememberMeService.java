package vn.edu.hcmute.service;

import vn.edu.hcmute.model.User;
import java.sql.SQLException;

public interface RememberMeService {

    // Generate and store a persistent login token
    String createToken(int userId) throws SQLException;

    // Authenticate a user using a persistent login token
    User authenticate(String token) throws SQLException;

    // Revoke a persistent login token
    void revokeToken(String token) throws SQLException;
}
