package com.aptitude.learning.e2buddy.School.Admin.DataClass;

public class StudentTestData {
    private int studentId,marks;
    private String studentName, sectionName,teacherRemark,rollNo;


    public StudentTestData(int studentId, String studentName, String sectionName, int marks, String teacherRemark, String rollNo) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.sectionName = sectionName;
        this.marks = marks;
        this.teacherRemark = teacherRemark;
        this.rollNo = rollNo;
    }


    public String getStudentName() {
        return studentName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public int getMarks() {
        return marks;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getTeacherRemark() {
        return teacherRemark;
    }

    public String getRollNo() {
        return rollNo;
    }
}
