package com.capsule.dto;

/**
 * DTO representing a message input from the client.
 * `deliveryDate` must be in the format yyyy-MM-dd.
 */
public class MessageRequest {

    private String title;
    private String content;
    private String deliveryDate; // Format: yyyy-MM-dd

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
