package com.example.ashwin.shopperspitstop;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ashwin on 6/3/2019.
 */
public class PageAdapter extends FragmentStatePagerAdapter {
    private String[] title = {"Shop Names", "Shopping History"};

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];

    }

    @Override
    public Fragment getItem(int position) {
        return TabFragment.getInstance(position);
    }
}
