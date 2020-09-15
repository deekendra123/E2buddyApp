package com.aptitude.learning.e2buddy.Preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.aptitude.learning.e2buddy.School.Admin.DataClass.SchoolData;

public class SchoolPreference {

    private static final String SHARED_PREF_NAME = "e2buddyappschoolinfo";
    private static final String KEY_SCHOOL_NAME = "keyschoolname";
    private static final String KEY_SCHOOL_CODE = "keyschoolcode";
    private static final String KEY_SCHOOL_ID = "keyid";
    private static final String KEY_SCHOOL_CITY = "keyschoolcity";
    private static final String KEY_SCHOOL_STATE = "keyschoolstate";
    private static final String KEY_SCHOOL_PINCODE  = "keyschoolpincode";
    private static final String KEY_TYPE = "keytype";
    private static final String KEY_SYSTEM_TAKEN = "keyschoolsystemtaken";
    private static final String KEY_SCHOOL_LOGO = "keyschoollogo";




    private static SchoolPreference mInstance;
    private static Context mCtx;

    private SchoolPreference(Context context) {
        mCtx = context;
    }

    public static synchronized SchoolPreference getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SchoolPreference(context);
        }
        return mInstance;
    }

    public void putSchoolInfo(SchoolData schoolData) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_SCHOOL_ID, schoolData.getSchoolId());
        editor.putString(KEY_SCHOOL_NAME, schoolData.getSchoolName());
        editor.putString(KEY_SCHOOL_CODE, schoolData.getSchoolCode());
        editor.putString(KEY_SCHOOL_CITY, schoolData.getSchoolCity());
        editor.putString(KEY_SCHOOL_STATE, schoolData.getSchoolState());
        editor.putString(KEY_SCHOOL_PINCODE, schoolData.getSchoolPincode());
        editor.putString(KEY_TYPE, schoolData.getSchoolType());
        editor.putString(KEY_SYSTEM_TAKEN, schoolData.getSchoolSystemTaken());
        editor.putString(KEY_SCHOOL_LOGO, schoolData.getSchoolLogo());
        editor.apply();
    }


    //this method will give the logged in user
    public SchoolData getSchoolInfo() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new SchoolData(
                sharedPreferences.getInt(KEY_SCHOOL_ID, -1),
                sharedPreferences.getString(KEY_SCHOOL_NAME, null),
                sharedPreferences.getString(KEY_SCHOOL_CODE, null),
                sharedPreferences.getString(KEY_SCHOOL_CITY, null),
                sharedPreferences.getString(KEY_SCHOOL_STATE, null),
                sharedPreferences.getString(KEY_SCHOOL_PINCODE, null),
                sharedPreferences.getString(KEY_TYPE, null),
                sharedPreferences.getString(KEY_SYSTEM_TAKEN, null),
                sharedPreferences.getString(KEY_SCHOOL_LOGO, null)
                );
    }

}