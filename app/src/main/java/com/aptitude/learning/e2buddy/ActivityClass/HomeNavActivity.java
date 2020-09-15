    package com.aptitude.learning.e2buddy.ActivityClass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.aptitude.learning.e2buddy.FragmentClass.TrickQuizFragment;
import com.aptitude.learning.e2buddy.FragmentClass.WordOfTheDayFragment;
import com.aptitude.learning.e2buddy.FragmentClass.WordPuzzleFragment;
import com.aptitude.learning.e2buddy.ObjectClass.User;
import com.aptitude.learning.e2buddy.Preference.SessionManager;
import com.aptitude.learning.e2buddy.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.lang.reflect.Field;

import static com.aptitude.learning.e2buddy.FragmentClass.TrickQuizFragment.mCurrentPage;

    public class HomeNavActivity extends AppCompatActivity {

    Fragment currentFragment = null;
    private TextView tvUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nav);
        tvUserName = findViewById(R.id.tvUserName);



        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        final User user = SessionManager.getInstance(this).getUser();
        tvUserName.setText("Welcome "+user.getUsername());

        navigation.setItemIconTintList(null);

        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);


                        switch (item.getItemId()) {


                            case R.id.navigation_trick_quiz:

                                item.setChecked(true);

                                if (!(currentFragment instanceof TrickQuizFragment)) {
                                    TrickQuizFragment fragment = new TrickQuizFragment();
                                    loadFragment(fragment);
                                }

                                break;


                            case R.id.navigation_word:
                                item.setChecked(true);

                                SharedPreferences sharedPreferences = getSharedPreferences("resumequiz", MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                editor1.putInt("position", mCurrentPage);
                                editor1.commit();

                                if (!(currentFragment instanceof WordOfTheDayFragment)) {
                                    WordOfTheDayFragment wordOfTheDayFragment = new WordOfTheDayFragment();
                                    loadFragment(wordOfTheDayFragment);
                                }
                                break;

                            case R.id.navigation_puzzle:
                                item.setChecked(true);

                                SharedPreferences sharedPreferences1 = getSharedPreferences("resumequiz", MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = sharedPreferences1.edit();
                                editor2.putInt("position", mCurrentPage);
                                editor2.commit();


                                if (!(currentFragment instanceof WordPuzzleFragment)) {
                                    WordPuzzleFragment wordPuzzleFragment = new WordPuzzleFragment();
                                    loadFragment(wordPuzzleFragment);
                                }
                                break;



                        }
                        return false;
                    }
                });

        loadFragment(new TrickQuizFragment());
    }


    private boolean loadFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
        return true;

    }

    @Override
    public void onBackPressed() {


        new AlertDialog.Builder(HomeNavActivity.this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences preferences =getSharedPreferences("nextquiz", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();

                        SharedPreferences sharedPreferences = getSharedPreferences("resumequiz", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.putInt("position", mCurrentPage);
                        editor1.commit();

                        HomeNavActivity.super.onBackPressed();
                    }
                }).create().show();

    }

    public static class BottomNavigationViewHelper {

        @SuppressLint("RestrictedApi")
        public static void removeShiftMode(BottomNavigationView view) {
            //this will remove shift mode for bottom navigation view
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    item.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
                    // set once again checked value, so view will be updated
                    item.setChecked(item.getItemData().isChecked());
                }

            } catch (NoSuchFieldException e) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
            } catch (IllegalAccessException e) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
            }
        }
    }





}
