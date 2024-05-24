package com.example.yellow;

public class Job {
    private int id;
    private String jobTitle;
    private String companyName;
    private String employmentType;
    private String location;
    private String requiredSkills;
    private String experienceLevel;
    private String educationLevel;
    private String salaryRange;
    private String createdAt;
    private int userId;

    public Job(int id, String jobTitle, String companyName, String employmentType, String location,
               String requiredSkills, String experienceLevel, String educationLevel, String salaryRange,
               String createdAt, int userId) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.employmentType = employmentType;
        this.location = location;
        this.requiredSkills = requiredSkills;
        this.experienceLevel = experienceLevel;
        this.educationLevel = educationLevel;
        this.salaryRange = salaryRange;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getLocation() {
        return location;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getUserId() {
        return userId;
    }
}
