package com.aptitude.learning.e2buddy.Preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.aptitude.learning.e2buddy.ActivityClass.LoginActivity;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.SuperAdminData;

public class SchoolSuperAdminSessionManager {

    private static final String SHARED_PREF_NAME = "e2buddyappsuperadmin";
    private static final String KEY_USERNAME = "keysuperadminname";
    private static final String KEY_EMAIL = "keysuperadminemail";
    private static final String KEY_ID = "keysuperadminid";
    private static final String KEY_SCHOOL_CODE = "keyschoolcode";
    private static final String KEY_ADMIN_IMAGE = "keysuperadminimage";
    private static final String KEY_ADMIN_PHONE = "keysuperadminphone";

    private static SchoolSuperAdminSessionManager mInstance;
    private static Context mCtx;

    private SchoolSuperAdminSessionManager(Context context) {
        mCtx = context;
    }

    public static synchronized SchoolSuperAdminSessionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SchoolSuperAdminSessionManager(context);
        }
        return mInstance;
    }

    public void userLogin(SuperAdminData user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getSuperAdminId());
        editor.putString(KEY_USERNAME, user.getSuperAdminName());
        editor.putString(KEY_EMAIL, user.getSuperAdminEmail());
        editor.putString(KEY_ADMIN_PHONE, user.getSuperAdminPhone());
        editor.putString(KEY_ADMIN_IMAGE, user.getSuperAdminImage());
        editor.putString(KEY_SCHOOL_CODE, user.getSchoolCode());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    public SuperAdminData getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new SuperAdminData(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_ADMIN_PHONE, null),
                sharedPreferences.getString(KEY_ADMIN_IMAGE, null),
                sharedPreferences.getString(KEY_SCHOOL_CODE, null)
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