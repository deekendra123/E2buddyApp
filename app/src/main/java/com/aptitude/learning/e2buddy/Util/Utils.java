package com.aptitude.learning.e2buddy.Util;

import android.app.ProgressDialog;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aptitude.learning.e2buddy.ActivityClass.LoginActivity;
import com.aptitude.learning.e2buddy.ApplicationClass.E2buddyApp;

public class Utils {

    public static ProgressDialog progressDialog;

    public static void showToast(String message){

        Toast.makeText(E2buddyApp.getInstance(), message,Toast.LENGTH_SHORT).show();
    }

    public static void showProgressBar(){
        progressDialog = new ProgressDialog(E2buddyApp.getInstance());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
    }
    public static void hideProgressBar(){
        progressDialog.dismiss();
    }

}
