package com.example.yellow;

public class JobResponse {
    private String jobTitle;
    private String message;
    private String status;
    private int companyid;
    private int userid;

    public JobResponse(String jobTitle, String message, String status) {
        this.jobTitle = jobTitle;
        this.message = message;
        this.status = status;
    }

    public JobResponse(String jobTitle, String message, String status, int companyid, int userid) {
        this.jobTitle = jobTitle;
        this.message = message;
        this.status = status;
        this.companyid = companyid;
        this.userid = userid;
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
    public int getCompanyid(){return companyid;}
    public int getUserid(){return userid;}
}
