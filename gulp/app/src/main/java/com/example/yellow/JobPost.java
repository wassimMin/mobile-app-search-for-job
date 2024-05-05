package com.example.yellow;

public class JobPost {
    private int jobid;
    private String title;
    private String description;
    private String companyName;
    private String location;
    private String category;
    private String salaryRange;

    public JobPost(int jobid,String title, String description, String companyName, String location, String category, String salaryRange) {
        this.jobid = jobid;
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.location = location;
        this.category = category;
        this.salaryRange = salaryRange;
    }
    public int getId(){
        return jobid;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }
}

