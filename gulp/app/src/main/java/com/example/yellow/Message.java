package com.example.yellow;

public class Message {
    private int messageId;
    private int senderId;
    private String messageContent;
    private String userType;

    public Message(int messageId, String messageContent, int senderId, String userType) {
        this.messageId = messageId;
        this.messageContent = messageContent;
        this.senderId = senderId;
        this.userType = userType;
    }


    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
