package com.aptitude.learning.e2buddy.School.Admin.DataClass;

public class ExamStatus {
    private int userId;
    private String name, sectionName, status, admissionNo, rollNo;

    public ExamStatus(int userId, String name, String sectionName, String status, String admissionNo, String rollNo) {
        this.userId = userId;
        this.name = name;
        this.sectionName = sectionName;
        this.status = status;
        this.admissionNo = admissionNo;
        this.rollNo = rollNo;
    }


    public int getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public String getName() {
        return name;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getRollNo() {
        return rollNo;
    }
}
