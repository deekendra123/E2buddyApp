package com.aptitude.learning.e2buddy.ObjectClass;

public class SchoolData {
    int id;
    String schoolName, schoolCode, city, state, pincode, type;

    public SchoolData(int id, String schoolName, String schoolCode, String city, String state, String pincode, String type) {
        this.id = id;
        this.schoolName = schoolName;
        this.schoolCode = schoolCode;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
