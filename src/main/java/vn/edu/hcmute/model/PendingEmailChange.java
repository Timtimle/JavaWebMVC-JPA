package vn.edu.hcmute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PendingEmailChanges")
public class PendingEmailChange {
    @Id
    @Column(name = "user_id")
    private int userId;
    @Column(name = "new_email", nullable = false, length = 255)
    private String newEmail;
    @Column(name = "code_hash", nullable = false, length = 64)
    private String codeHash;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    @Column(name = "attempts", nullable = false)
    private int attempts;

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getNewEmail() { return newEmail; }
    public void setNewEmail(String newEmail) { this.newEmail = newEmail; }
    public String getCodeHash() { return codeHash; }
    public void setCodeHash(String codeHash) { this.codeHash = codeHash; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public int getAttempts() { return attempts; }
    public void setAttempts(int attempts) { this.attempts = attempts; }
}
