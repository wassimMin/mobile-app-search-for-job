package com.example.yellow;

public class AplicationItem {
    private String id;
    private String userName;
    private String jobName;
    private byte[] cvPdfData;

    public AplicationItem(String id,String userName, String jobName, byte[] cvPdfData) {
        this.id = id;
        this.userName = userName;
        this.jobName = jobName;
        this.cvPdfData = cvPdfData;
    }


    public String getId(){return id;}
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public byte[] getCvPdfData() {
        return cvPdfData;
    }

    public void setCvPdfData(byte[] cvPdfData) {
        this.cvPdfData = cvPdfData;
    }
}
