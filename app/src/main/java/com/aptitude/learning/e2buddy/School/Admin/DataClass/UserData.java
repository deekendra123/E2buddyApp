package com.aptitude.learning.e2buddy.School.Admin.DataClass;

public class UserData {
    private int userId;
    private String userName;
    private int sectionId,classId;
    private String sectionName;
    private String admissionNo,rollNo;


    public UserData(int userId, String userName, int sectionId, int classId, String sectionName) {
        this.userId = userId;
        this.userName = userName;
        this.sectionId = sectionId;
        this.classId = classId;
        this.sectionName = sectionName;
    }

    public UserData(int userId, String userName, int sectionId, int classId, String sectionName, String admissionNo, String rollNo) {
        this.userId = userId;
        this.userName = userName;
        this.sectionId = sectionId;
        this.classId = classId;
        this.sectionName = sectionName;
        this.admissionNo = admissionNo;
        this.rollNo = rollNo;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getSectionId() {
        return sectionId;
    }

    public int getClassId() {
        return classId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public String getRollNo() {
        return rollNo;
    }
}
