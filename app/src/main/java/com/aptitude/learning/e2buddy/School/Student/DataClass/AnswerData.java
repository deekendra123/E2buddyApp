package com.aptitude.learning.e2buddy.School.Student.DataClass;

public class AnswerData {
    int questionId,status;
    String question, corrAnswer,userAnswer,dateTime,date,time;

    public AnswerData(int questionId, String question, String corrAnswer, String userAnswer) {
        this.questionId = questionId;
        this.question = question;
        this.corrAnswer = corrAnswer;
        this.userAnswer = userAnswer;
    }


    public AnswerData(int questionId, String userAnswer) {
        this.questionId = questionId;
        this.userAnswer = userAnswer;
    }

    public AnswerData(int questionId, String question, String corrAnswer, String userAnswer, String dateTime) {
        this.questionId = questionId;
        this.question = question;
        this.corrAnswer = corrAnswer;
        this.userAnswer = userAnswer;
        this.dateTime = dateTime;
    }

    public AnswerData() {
    }

    public AnswerData(int questionId, int status, String userAnswer, String dateTime) {
        this.questionId = questionId;
        this.status = status;
        this.userAnswer = userAnswer;
        this.dateTime = dateTime;
    }

    public AnswerData(int questionId, int status, String userAnswer, String date, String time) {
        this.questionId = questionId;
        this.status = status;
        this.userAnswer = userAnswer;
        this.date = date;
        this.time = time;
    }


    public int getQuestionId() {
        return questionId;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrAnswer() {
        return corrAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public int getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
