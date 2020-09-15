package com.aptitude.learning.e2buddy.School.Admin.DataClass;

public class MarkData {
    private int classTestMark;
    private String teacherRemark;

    public MarkData() {
    }

    public int getClassTestMark() {
        return classTestMark;
    }

    public String getTeacherRemark() {
        return teacherRemark;
    }

    public void setClassTestMark(int classTestMark) {
        this.classTestMark = classTestMark;
    }

    public void setTeacherRemark(String teacherRemark) {
        this.teacherRemark = teacherRemark;
    }
}
