package com.example.yellow;

public class Job {
    private String id;
    private String jobName;
    private String jobPosition;
    private String jobRequirements;
    private String jobSalaire;
    public Job(String id,String jobName, String jobPosition, String jobRequirements,String jobSalaire) {
        this.id = id;
        this.jobName = jobName;
        this.jobPosition = jobPosition;
        this.jobRequirements = jobRequirements;
        this.jobSalaire = jobSalaire;
    }
    public String getId(){
        return id;
    }
    public String getJobName() {
        return jobName;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public String getJobRequirements() {
        return jobRequirements;
    }
    public String getJobSalaire(){return jobSalaire;}
}
