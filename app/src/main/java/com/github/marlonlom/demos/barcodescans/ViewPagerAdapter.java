package com.github.marlonlom.demos.barcodescans;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Viewpager adapter class
 *
 * @author marlonlom
 */
final class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;

    ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>(2);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position >= 0 && position < 2) {
            return "Dummy,Camera".split(",")[position];
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }
}
