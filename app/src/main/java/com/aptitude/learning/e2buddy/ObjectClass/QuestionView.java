package com.aptitude.learning.e2buddy.ObjectClass;

import java.io.Serializable;

/**
 * Created by Matrix on 26-11-2018.
 */

public class QuestionView implements Serializable {


    private int questionid;
    private String questionImage;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer;
    private String description;
    private int wordCoachId;
    private String questionHeading;

    public QuestionView(int questionid, String questionImage, String option1, String option2, String option3, String option4, String answer, String description, int wordCoachId, String questionHeading) {
        this.questionid = questionid;
        this.questionImage = questionImage;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.description = description;
        this.wordCoachId = wordCoachId;
        this.questionHeading = questionHeading;
    }


    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWordCoachId() {
        return wordCoachId;
    }

    public void setWordCoachId(int wordCoachId) {
        this.wordCoachId = wordCoachId;
    }

    public String getQuestionHeading() {
        return questionHeading;
    }

    public void setQuestionHeading(String questionHeading) {
        this.questionHeading = questionHeading;
    }
}
