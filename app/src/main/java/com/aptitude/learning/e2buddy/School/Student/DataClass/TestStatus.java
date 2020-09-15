package com.aptitude.learning.e2buddy.School.Student.DataClass;

public class TestStatus {
    private int classTestId,status,classTestMark;
    private String teacherRemark;

    public TestStatus(int classTestId, int status, int classTestMark, String teacherRemark) {
        this.classTestId = classTestId;
        this.status = status;
        this.classTestMark = classTestMark;
        this.teacherRemark = teacherRemark;
    }

    public int getStatus() {
        return status;
    }

    public int getClassTestMark() {
        return classTestMark;
    }

    public String getTeacherRemark() {
        return teacherRemark;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setClassTestMark(int classTestMark) {
        this.classTestMark = classTestMark;
    }

    public void setTeacherRemark(String teacherRemark) {
        this.teacherRemark = teacherRemark;
    }

    public int getClassTestId() {
        return classTestId;
    }
}
