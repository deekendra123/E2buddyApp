package com.aptitude.learning.e2buddy.School.Admin.DataClass;

public class ClassData {
    int classid;
    String className;

    public ClassData(int classid, String className) {
        this.classid = classid;
        this.className = className;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
