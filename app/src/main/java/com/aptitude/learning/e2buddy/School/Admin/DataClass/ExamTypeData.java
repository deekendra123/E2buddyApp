package com.aptitude.learning.e2buddy.School.Admin.DataClass;

public class ExamTypeData {

    private int examTypeId;
    private String examName,startTime,endTime;
    private int examAddedBy;

    public ExamTypeData(int examTypeId, String examName, String startTime, String endTime, int examAddedBy) {
        this.examTypeId = examTypeId;
        this.examName = examName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.examAddedBy = examAddedBy;
    }

    public ExamTypeData(int examTypeId, String examName, int examAddedBy) {
        this.examTypeId = examTypeId;
        this.examName = examName;
        this.examAddedBy = examAddedBy;
    }

    public int getExamTypeId() {
        return examTypeId;
    }

    public String getExamName() {
        return examName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getExamAddedBy() {
        return examAddedBy;
    }
}
