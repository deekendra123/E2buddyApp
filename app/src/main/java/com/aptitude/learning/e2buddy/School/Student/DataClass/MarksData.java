package com.aptitude.learning.e2buddy.School.Student.DataClass;

public class MarksData {
    private int classTestId, userId, correctAnswer, wrongAnswer, classTestMark, status,teacherRemark;
    private String date,time;

    public MarksData() {
    }

    public int getClassTestId() {
        return classTestId;
    }

    public void setClassTestId(int classTestId) {
        this.classTestId = classTestId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(int wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public int getClassTestMark() {
        return classTestMark;
    }

    public void setClassTestMark(int classTestMark) {
        this.classTestMark = classTestMark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTeacherRemark() {
        return teacherRemark;
    }

    public void setTeacherRemark(int teacherRemark) {
        this.teacherRemark = teacherRemark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
