package com.aptitude.learning.e2buddy.School.Student.DataClass;

public class QuestionModel {

    private String question;
    private int seleectedAnswerPosition;
    private boolean op1Sel,op2Sel,op3Sel,op4Sel; // options

    public boolean isOp1Sel() {
        return op1Sel;
    }

    public void setOp1Sel(boolean op1Sel) {
        this.op1Sel = op1Sel;
        if(op1Sel){ // To make sure only one option is selected at a time
            setOp2Sel(false);
            setOp3Sel(false);
        }
    }

    public boolean isOp2Sel() {
        return op2Sel;
    }

    public void setOp2Sel(boolean op2Sel) {
        this.op2Sel = op2Sel;
        if(op2Sel){
            setOp1Sel(false);
            setOp3Sel(false);
        }
    }

    public boolean isOp3Sel() {
        return op3Sel;
    }

    public void setOp3Sel(boolean op3Sel) {
        this.op3Sel = op3Sel;
        if(op3Sel){
            setOp2Sel(false);
            setOp1Sel(false);
        }
    }

    public boolean isOp4Sel() {
        return op3Sel;
    }

    public void setOp4Sel(boolean op4Sel) {
        this.op4Sel = op4Sel;
        if(op4Sel){
            setOp2Sel(false);
            setOp1Sel(false);
            setOp3Sel(false);
        }
    }

    public int getSeleectedAnswerPosition() {
        return seleectedAnswerPosition;
    }

    public void setSeleectedAnswerPosition(int seleectedAnswerPosition) {
        this.seleectedAnswerPosition = seleectedAnswerPosition;
    }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

}
