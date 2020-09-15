package com.aptitude.learning.e2buddy.School.Admin.DataClass;

public class SubjectData {

    int subjectId, classId;
    String subjectName;


    public SubjectData(int subjectId, int classId, String subjectName) {
        this.subjectId = subjectId;
        this.classId = classId;
        this.subjectName = subjectName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getClassId() {
        return classId;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
