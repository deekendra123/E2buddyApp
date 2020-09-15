package com.aptitude.learning.e2buddy.School.Admin.DataClass;

public class QuestionData {
    int questionid,classTestId;
    String question, option1, option2, option3, option4, answer, description;

    public QuestionData(int questionid) {
        this.questionid = questionid;
    }

    public QuestionData(int questionid, String question, String option1, String option2, String option3, String option4) {
        this.questionid = questionid;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
    }

    public QuestionData(int questionid, String question, String option1, String option2, String option3, String option4, String answer, String description) {
        this.questionid = questionid;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.description = description;
    }

    public QuestionData(String question, String option1, String option2, String answer) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.answer = answer;
    }

    public QuestionData(int questionid, int classTestId, String question, String option1, String option2, String option3, String option4, String answer, String description) {
        this.questionid = questionid;
        this.classTestId = classTestId;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.description = description;
    }


    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }


    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getAnswer() {
        return answer;
    }

    public String getDescription() {
        return description;
    }

    public int getClassTestId() {
        return classTestId;
    }
}
