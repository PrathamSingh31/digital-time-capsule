package com.capsule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO representing a message input from the client.
 * `deliveryDate` must be in the format yyyy-MM-dd.
 */
public class MessageRequest {

    @NotBlank(message = "Title must not be empty")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @NotBlank(message = "Content must not be empty")
    @Size(max = 1000, message = "Content cannot exceed 1000 characters")
    private String content;

    @NotBlank(message = "Delivery date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Delivery date must be in yyyy-MM-dd format")
    private String deliveryDate;

    public MessageRequest() {
    }

    public MessageRequest(String title, String content, String deliveryDate) {
        this.title = title;
        this.content = content;
        this.deliveryDate = deliveryDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return "MessageRequest{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                '}';
    }
}
