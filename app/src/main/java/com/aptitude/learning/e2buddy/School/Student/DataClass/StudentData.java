package com.aptitude.learning.e2buddy.School.Student.DataClass;

public class StudentData {


    private int id,schoolId,classId,sectionId;
    String studentName, studentEmail, studentDob, studentImage, schoolName,schoolCode,city,state,pincode,type;
    private int systemTaken;
    String schoolLogo;

    public StudentData(int id, String studentName, String studentEmail, String studentDob, String studentImage, int schoolId, String schoolName,

                       String schoolCode, String city, String state, String pincode, String type, int systemTaken, String schoolLogo, int classId, int sectionId) {
        this.id = id;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentDob = studentDob;
        this.studentImage = studentImage;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.schoolCode = schoolCode;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.type = type;
        this.systemTaken = systemTaken;
        this.schoolLogo = schoolLogo;
        this.classId = classId;
        this.sectionId = sectionId;
    }

    public int getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getStudentDob() {
        return studentDob;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public String getType() {
        return type;
    }

    public int getSystemTaken() {
        return systemTaken;
    }

    public String getSchoolLogo() {
        return schoolLogo;
    }

    public String getStudentImage() {
        return studentImage;
    }


    public int getSchoolId() {
        return schoolId;
    }

    public int getClassId() {
        return classId;
    }

    public int getSectionId() {
        return sectionId;
    }
}
