package vn.edu.hcmute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Videos")
public class Video {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="video_id")
    private long id;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="user_id", nullable=false)
    private User owner;

    @Column(name="title", nullable=false, columnDefinition="nvarchar(255)")
    private String title;

    @Column(name="description", columnDefinition="nvarchar(max)")
    private String description;

    @Column(name="file_name", nullable=false, length=255)
    private String fileName;

    @Column(name="views", nullable=false)
    private long views;

    @Column(name="created_at", insertable=false,updatable=false)
    private LocalDateTime createdAt;

    public long getId(){return id;}
    public User getOwner(){return owner;}
    public void setOwner(User owner){this.owner=owner;}
    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}
    public String getFileName(){return fileName;}
    public void setFileName(String fileName){this.fileName=fileName;}
    public long getViews(){return views;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
