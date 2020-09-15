package com.aptitude.learning.e2buddy.Preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.aptitude.learning.e2buddy.ActivityClass.LoginActivity;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;
import com.aptitude.learning.e2buddy.School.Student.DataClass.StudentData;

public class SchoolStudentSessionManager {

    private static final String SHARED_PREF_NAME = "e2buddyappadmin";
    private static final String KEY_ID = "keyid";

    private static final String KEY_STUDENT_NAME = "studentName";
    private static final String KEY_STUDENT_EMAIL = "studentEmail";
    private static final String KEY_STUDENT_DOB = "studentDob";
    private static final String KEY_STUDENT_IMAGE = "studentImage";
    private static final String KEY_SCHOOL_ID = "schoolId";

    private static final String KEY_SCHOOL_NAME = "schoolName";

    private static final String KEY_SCHOOL_CODE = "schoolCode";
    private static final String KEY_SCHOOL_CITY = "city";
    private static final String KEY_SCHOOL_STATE = "state";
    private static final String KEY_SCHOOL_PINCODE = "pincode";

    private static final String KEY_SCHOOL_TYPE = "type";
    private static final String KEY_SYSTEM_TAKEN = "systemTaken";
    private static final String KEY_SCHOOL_LOGO = "schoolLogo";

    private static final String KEY_STUDENT_CLASS = "studentClassId";
    private static final String KEY_STUDENT_SECTION= "studentSectionId";



    private static SchoolStudentSessionManager mInstance;
    private static Context mCtx;

    private SchoolStudentSessionManager(Context context) {
        mCtx = context;
    }

    public static synchronized SchoolStudentSessionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SchoolStudentSessionManager(context);
        }
        return mInstance;
    }

    public void userLogin(StudentData user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_STUDENT_NAME, user.getStudentName());
        editor.putString(KEY_STUDENT_EMAIL, user.getStudentEmail());
        editor.putString(KEY_STUDENT_DOB, user.getStudentDob());
        editor.putString(KEY_STUDENT_IMAGE, user.getStudentImage());
        editor.putInt(KEY_SCHOOL_ID, user.getSchoolId());
        editor.putString(KEY_SCHOOL_NAME, user.getSchoolName());
        editor.putString(KEY_SCHOOL_CODE, user.getSchoolCode());
        editor.putString(KEY_SCHOOL_CITY, user.getCity());
        editor.putString(KEY_SCHOOL_STATE, user.getState());
        editor.putString(KEY_SCHOOL_PINCODE, user.getPincode());
        editor.putString(KEY_SCHOOL_TYPE, user.getType());
        editor.putInt(KEY_SYSTEM_TAKEN, user.getSystemTaken());
        editor.putString(KEY_SCHOOL_LOGO, user.getSchoolLogo());
        editor.putInt(KEY_STUDENT_CLASS, user.getClassId());
        editor.putInt(KEY_STUDENT_SECTION, user.getSectionId());

        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_NAME, null) != null;
    }

    public StudentData getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new StudentData(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_STUDENT_NAME, null),
                sharedPreferences.getString(KEY_STUDENT_EMAIL, null),
                sharedPreferences.getString(KEY_STUDENT_DOB, null),
                sharedPreferences.getString(KEY_STUDENT_IMAGE, null),

                sharedPreferences.getInt(KEY_SCHOOL_ID, -1),

                sharedPreferences.getString(KEY_SCHOOL_NAME, null),
                sharedPreferences.getString(KEY_SCHOOL_CODE, null),
                sharedPreferences.getString(KEY_SCHOOL_CITY, null),
                sharedPreferences.getString(KEY_SCHOOL_STATE, null),

                sharedPreferences.getString(KEY_SCHOOL_PINCODE, null),
                sharedPreferences.getString(KEY_SCHOOL_TYPE, null),
                sharedPreferences.getInt(KEY_SYSTEM_TAKEN, -1),
                sharedPreferences.getString(KEY_SCHOOL_LOGO, null),
                sharedPreferences.getInt(KEY_STUDENT_CLASS, -1),
                sharedPreferences.getInt(KEY_STUDENT_SECTION, -1)

                );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}