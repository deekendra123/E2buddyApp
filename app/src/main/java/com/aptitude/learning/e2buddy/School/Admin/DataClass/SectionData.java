package com.aptitude.learning.e2buddy.School.Admin.DataClass;

public class SectionData {
    private String section;
    private boolean isSelected = false;

    int sectionId, classId;
    String sectionName;

    public SectionData(int sectionId, int classId, String sectionName) {
        this.sectionId = sectionId;
        this.classId = classId;
        this.sectionName = sectionName;
    }

    public SectionData(int sectionId) {
        this.sectionId = sectionId;
    }



    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getSectionId() {
        return sectionId;
    }

    public int getClassId() {
        return classId;
    }

    public String getSectionName() {
        return sectionName;
    }

}
