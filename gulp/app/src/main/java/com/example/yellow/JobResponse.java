package com.example.yellow;

public class JobResponse {
    private String jobTitle;
    private String message;
    private String status;

    public JobResponse(String jobTitle, String message, String status) {
        this.jobTitle = jobTitle;
        this.message = message;
        this.status = status;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
