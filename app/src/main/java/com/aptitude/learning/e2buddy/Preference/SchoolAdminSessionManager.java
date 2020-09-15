package com.aptitude.learning.e2buddy.Preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.aptitude.learning.e2buddy.ActivityClass.LoginActivity;
import com.aptitude.learning.e2buddy.ObjectClass.User;
import com.aptitude.learning.e2buddy.School.Admin.DataClass.AdminData;

public class SchoolAdminSessionManager {

    private static final String SHARED_PREF_NAME = "e2buddyappadmin";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_ID = "keyid";
    private static final String KEY_SCHOOL_CODE = "keyschoolcode";
    private static final String KEY_ADMINT_IMAGE = "keyadminimage";

    private static SchoolAdminSessionManager mInstance;
    private static Context mCtx;

    private SchoolAdminSessionManager(Context context) {
        mCtx = context;
    }

    public static synchronized SchoolAdminSessionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SchoolAdminSessionManager(context);
        }
        return mInstance;
    }

    public void userLogin(AdminData user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_SCHOOL_CODE, user.getSchoolCode());
        editor.putString(KEY_ADMINT_IMAGE, user.getAdminImage());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    public AdminData getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new AdminData(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_SCHOOL_CODE, null),
                sharedPreferences.getString(KEY_ADMINT_IMAGE, null)
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