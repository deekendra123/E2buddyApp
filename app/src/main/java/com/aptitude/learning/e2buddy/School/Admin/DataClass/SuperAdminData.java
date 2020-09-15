package com.aptitude.learning.e2buddy.School.Admin.DataClass;

public class SuperAdminData {

    private int superAdminId;
    private String superAdminName, superAdminEmail, superAdminPhone, superAdminImage, schoolCode;


    public SuperAdminData(int superAdminId, String superAdminName, String superAdminEmail, String superAdminPhone, String superAdminImage, String schoolCode) {
        this.superAdminId = superAdminId;
        this.superAdminName = superAdminName;
        this.superAdminEmail = superAdminEmail;
        this.superAdminPhone = superAdminPhone;
        this.superAdminImage = superAdminImage;
        this.schoolCode = schoolCode;
    }

    public int getSuperAdminId() {
        return superAdminId;
    }

    public String getSuperAdminName() {
        return superAdminName;
    }

    public String getSuperAdminEmail() {
        return superAdminEmail;
    }

    public String getSuperAdminPhone() {
        return superAdminPhone;
    }

    public String getSuperAdminImage() {
        return superAdminImage;
    }

    public String getSchoolCode() {
        return schoolCode;
    }
}
