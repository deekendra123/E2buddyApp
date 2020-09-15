package com.aptitude.learning.e2buddy.School.SuperAdmin.ObjectData;

public class TeacherData {
    private int teacherId;
    String teacherName, teacherEmail, teacherPhone, dateTime;

    public TeacherData(int teacherId, String teacherName, String teacherEmail, String teacherPhone, String dateTime) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
        this.teacherPhone = teacherPhone;
        this.dateTime = dateTime;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public String getDateTime() {
        return dateTime;
    }
}
