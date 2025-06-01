package com.capsule.dto;

public class MessageRequest {
    private String content;
    private String title;
    private String deliveryDate;

    public MessageRequest() {}

    public MessageRequest(String content, String title, String deliveryDate) {
        this.content = content;
        this.title = title;
        this.deliveryDate = deliveryDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}