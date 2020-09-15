package com.aptitude.learning.e2buddy.School.Admin.DataClass;

public class ClassTestData {
 private int classTestId;
 private String classTestName;
 private int classId, sectionId, subjectId, maxMark, passMark;
 private String classTestDate;
 private int timeAllotted, totalQuestion, correctAnswerMark, wrongAnswerMark, notAttemptedMark, schoolCode, status, classTestAddedBy, classTestVisible;

    public ClassTestData(int classTestId, String classTestName, int classId, int sectionId, int subjectId, int maxMark, int passMark, String classTestDate, int timeAllotted, int totalQuestion, int correctAnswerMark, int wrongAnswerMark, int notAttemptedMark, int schoolCode, int status, int classTestAddedBy, int classTestVisible) {
        this.classTestId = classTestId;
        this.classTestName = classTestName;
        this.classId = classId;
        this.sectionId = sectionId;
        this.subjectId = subjectId;
        this.maxMark = maxMark;
        this.passMark = passMark;
        this.classTestDate = classTestDate;
        this.timeAllotted = timeAllotted;
        this.totalQuestion = totalQuestion;
        this.correctAnswerMark = correctAnswerMark;
        this.wrongAnswerMark = wrongAnswerMark;
        this.notAttemptedMark = notAttemptedMark;
        this.schoolCode = schoolCode;
        this.status = status;
        this.classTestAddedBy = classTestAddedBy;
        this.classTestVisible = classTestVisible;
    }

    public int getClassTestId() {
        return classTestId;
    }

    public String getClassTestName() {
        return classTestName;
    }

    public int getClassId() {
        return classId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getMaxMark() {
        return maxMark;
    }

    public int getPassMark() {
        return passMark;
    }

    public String getClassTestDate() {
        return classTestDate;
    }

    public int getTimeAllotted() {
        return timeAllotted;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public int getCorrectAnswerMark() {
        return correctAnswerMark;
    }

    public int getWrongAnswerMark() {
        return wrongAnswerMark;
    }

    public int getNotAttemptedMark() {
        return notAttemptedMark;
    }

    public int getSchoolCode() {
        return schoolCode;
    }

    public int getStatus() {
        return status;
    }

    public int getClassTestAddedBy() {
        return classTestAddedBy;
    }

    public int getClassTestVisible() {
        return classTestVisible;
    }
}
