package com.capsule.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_message")
public class UserMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "message_date_time", nullable = false)
    private LocalDateTime messageDateTime;

    @Column(nullable = false)
    private int year;

    @Column(name = "share_token", unique = true)
    private String shareToken; // ✅ NEW: Token for public sharing

    @Column(name = "image_url")
    private String imageUrl;

    private String videoUrl;

    // inside UserMessage.java
    private String imagePath;




    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    public UserMessage() {}

    // Getters and Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public LocalDateTime getMessageDateTime() { return messageDateTime; }

    public void setMessageDateTime(LocalDateTime messageDateTime) { this.messageDateTime = messageDateTime; }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public String getShareToken() { return shareToken; }

    public void setShareToken(String shareToken) { this.shareToken = shareToken; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

}
