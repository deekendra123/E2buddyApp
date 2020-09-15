package com.aptitude.learning.e2buddy.School.Student.DataClass;

public class ExamData {
    int examTypeId,examId;
    String examName;
    private int classId,sectionId,subjectId,maxMark,passMark;
    private String examDate,examStartTime,examStopTime;
    private int timeAllotted,totalQuestion,correctAnswerMark,wrongAnswerMark,notAttemptedMark,schoolCode,status,examAddedBy,examVisible,examResult;

    public ExamData(int examTypeId, int examId, String examName, int classId, int sectionId, int subjectId, int maxMark, int passMark, String examDate, String examStartTime, String examStopTime, int timeAllotted, int totalQuestion, int correctAnswerMark, int wrongAnswerMark, int notAttemptedMark, int schoolCode, int status, int examAddedBy, int examVisible, int examResult) {
        this.examTypeId = examTypeId;
        this.examId = examId;
        this.examName = examName;
        this.classId = classId;
        this.sectionId = sectionId;
        this.subjectId = subjectId;
        this.maxMark = maxMark;
        this.passMark = passMark;
        this.examDate = examDate;
        this.examStartTime = examStartTime;
        this.examStopTime = examStopTime;
        this.timeAllotted = timeAllotted;
        this.totalQuestion = totalQuestion;
        this.correctAnswerMark = correctAnswerMark;
        this.wrongAnswerMark = wrongAnswerMark;
        this.notAttemptedMark = notAttemptedMark;
        this.schoolCode = schoolCode;
        this.status = status;
        this.examAddedBy = examAddedBy;
        this.examVisible = examVisible;
        this.examResult = examResult;
    }

    public int getExamTypeId() {
        return examTypeId;
    }

    public int getExamId() {
        return examId;
    }

    public String getExamName() {
        return examName;
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

    public String getExamDate() {
        return examDate;
    }

    public String getExamStartTime() {
        return examStartTime;
    }

    public String getExamStopTime() {
        return examStopTime;
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

    public int getExamAddedBy() {
        return examAddedBy;
    }

    public int getExamVisible() {
        return examVisible;
    }

    public int getExamResult() {
        return examResult;
    }
}
