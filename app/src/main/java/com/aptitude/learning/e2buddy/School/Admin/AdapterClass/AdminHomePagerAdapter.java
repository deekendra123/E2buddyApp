package com.aptitude.learning.e2buddy.School.Admin.AdapterClass;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.Class10Fragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.Class11Fragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.Class12Fragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.Class1Fragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.Class2Fragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.Class3Fragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.Class4Fragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.Class5Fragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.Class6Fragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.Class7Fragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.Class8Fragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.Class9Fragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.LKGFragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.NurseryFragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.PreNurseryFragment;
import com.aptitude.learning.e2buddy.School.Admin.FragmentClass.UKGFragment;

public class AdminHomePagerAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public AdminHomePagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PreNurseryFragment preNurseryFragment = new PreNurseryFragment();
                return preNurseryFragment;
            case 1:
                NurseryFragment nurseryFragment = new NurseryFragment();
                return nurseryFragment;
            case 2:
                LKGFragment lkgFragment = new LKGFragment();
                return lkgFragment;
            case 3:
                UKGFragment ukgFragment = new UKGFragment();
                return ukgFragment;
            case 4:
                Class1Fragment class1Fragment = new Class1Fragment();
                return class1Fragment;
            case 5:
                Class2Fragment class2Fragment = new Class2Fragment();
                return class2Fragment;
            case 6:
                Class3Fragment class3Fragment = new Class3Fragment();
                return class3Fragment;
            case 7:
                Class4Fragment class4Fragment = new Class4Fragment();
                return class4Fragment;
            case 8:
                Class5Fragment class5Fragment = new Class5Fragment();
                return class5Fragment;
            case 9:
                Class6Fragment class6Fragment = new Class6Fragment();
                return class6Fragment;
            case 10:
                Class7Fragment class7Fragment = new Class7Fragment();
                return class7Fragment;
            case 11:
                Class8Fragment class8Fragment = new Class8Fragment();
                return class8Fragment;
            case 12:
                Class9Fragment class9Fragment = new Class9Fragment();
                return class9Fragment;
            case 13:
                Class10Fragment class10Fragment = new Class10Fragment();
                return class10Fragment;
            case 14:
                Class11Fragment class11Fragment = new Class11Fragment();
                return class11Fragment;

            case 15:
                Class12Fragment class12Fragment2 = new Class12Fragment();
                return class12Fragment2;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
