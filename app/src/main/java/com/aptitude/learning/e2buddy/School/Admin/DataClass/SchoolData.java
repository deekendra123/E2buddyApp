package com.aptitude.learning.e2buddy.School.Admin.DataClass;

public class SchoolData {
    int schoolId;
    String schoolName, schoolCode, schoolCity, schoolState, schoolPincode, schoolType, schoolSystemTaken, schoolLogo;

    public SchoolData(int schoolId, String schoolName, String schoolCode, String schoolCity, String schoolState, String schoolPincode, String schoolType, String schoolSystemTaken, String schoolLogo) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.schoolCode = schoolCode;
        this.schoolCity = schoolCity;
        this.schoolState = schoolState;
        this.schoolPincode = schoolPincode;
        this.schoolType = schoolType;
        this.schoolSystemTaken = schoolSystemTaken;
        this.schoolLogo = schoolLogo;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public String getSchoolCity() {
        return schoolCity;
    }

    public String getSchoolState() {
        return schoolState;
    }

    public String getSchoolPincode() {
        return schoolPincode;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public String getSchoolSystemTaken() {
        return schoolSystemTaken;
    }

    public String getSchoolLogo() {
        return schoolLogo;
    }
}

