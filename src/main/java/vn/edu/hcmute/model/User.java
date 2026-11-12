package vn.edu.hcmute.model;

import java.io.Serializable;
import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "username", nullable = false, length = 100, unique = true)
    private String username;

    @Column(name = "fullname", columnDefinition = "nvarchar(255)")
    private String fullName;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "roleid", nullable = false)
    private int roleId;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "createdDate", insertable = false, updatable = false)
    private Timestamp createdDate;

    public User() {
    }

    public User(int id, String email, String username,
                String fullName, String password,
                String avatar, int roleId, String phone,
                Timestamp createdDate) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.avatar = avatar;
        this.roleId = roleId;
        this.phone = phone;
        this.createdDate = createdDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
}
