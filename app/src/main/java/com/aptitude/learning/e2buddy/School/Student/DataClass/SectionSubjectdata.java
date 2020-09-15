package com.aptitude.learning.e2buddy.School.Student.DataClass;

public class SectionSubjectdata {
    int Id;
    String name;


    public SectionSubjectdata(int id, String name) {
        Id = id;
        this.name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
