package com.aptitude.learning.e2buddy.School.Student.DataClass;

import java.io.Serializable;

/**
 * Created by Matrix on 26-11-2018.
 */

public class QuestionData {


    private int questionid;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer;
    private String description;

    private boolean isSelected = false;
    private int buttonId;


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

    public QuestionData(int questionid, String question, String answer) {
        this.questionid = questionid;
        this.question = question;
        this.answer = answer;
    }

    public int getQuestionid() {
        return questionid;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
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

    public int getButtonId() {
        return buttonId;
    }

    public void setButtonId(int buttonId) {
        this.buttonId = buttonId;
    }
}
