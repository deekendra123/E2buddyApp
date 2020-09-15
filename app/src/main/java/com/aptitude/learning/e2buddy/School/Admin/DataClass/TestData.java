package com.aptitude.learning.e2buddy.School.Admin.DataClass;

public class TestData {
    int classTestId,classId,subjectId,maxMark,passMark,classTestVisible,classTestAddedBy,correctAnswerMark,wrongAnswerMark,notAttemptedMark;
    String classTestName,classTestDate,timeAlloted,totalQuestion,status;

    public TestData(int classTestId, int classId, int subjectId, int maxMark, int passMark, String classTestDate, int classTestVisible, String classTestName,int classTestAddedBy, String totalQuestion, String status) {
        this.classTestId = classTestId;
        this.classId = classId;
        this.subjectId = subjectId;
        this.maxMark = maxMark;
        this.passMark = passMark;
        this.classTestDate = classTestDate;
        this.classTestVisible = classTestVisible;
        this.classTestName = classTestName;
        this.classTestAddedBy = classTestAddedBy;
        this.totalQuestion = totalQuestion;
        this.status = status;
    }

    public TestData(int classTestId, int classId, int subjectId, int maxMark, int passMark, int classTestAddedBy, int correctAnswerMark,
                    int wrongAnswerMark, int notAttemptedMark, String classTestName, String classTestDate, String timeAlloted) {
        this.classTestId = classTestId;
        this.classId = classId;
        this.subjectId = subjectId;
        this.maxMark = maxMark;
        this.passMark = passMark;
        this.classTestAddedBy = classTestAddedBy;
        this.correctAnswerMark = correctAnswerMark;
        this.wrongAnswerMark = wrongAnswerMark;
        this.notAttemptedMark = notAttemptedMark;
        this.classTestName = classTestName;
        this.classTestDate = classTestDate;
        this.timeAlloted = timeAlloted;
    }


    public int getClassTestId() {
        return classTestId;
    }

    public int getClassId() {
        return classId;
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

    public int getClassTestVisible() {
        return classTestVisible;
    }

    public String getClassTestName() {
        return classTestName;
    }

    public String getClassTestDate() {
        return classTestDate;
    }

    public int getClassTestAddedBy() {
        return classTestAddedBy;
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

    public String getTimeAlloted() {
        return timeAlloted;
    }

    public String getTotalQuestion() {
        return totalQuestion;
    }

    public String getStatus() {
        return status;
    }
}
