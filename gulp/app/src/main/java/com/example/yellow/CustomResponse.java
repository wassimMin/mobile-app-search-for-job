package com.example.yellow;

public class CustomResponse {
    private int applicationId;
    private String responseText;
    private String message;

    public CustomResponse(int applicationId, String responseText, String message) {
        this.applicationId = applicationId;
        this.responseText = responseText;
        this.message = message;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public String getResponseText() {
        return responseText;
    }

    public String getMessage() {
        return message;
    }
}
