package com.example.abhi.clean_india.com.example.abhi.clean_india.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Abhi on 20-Jan-16.
 */
public class TabsPageAdapter extends FragmentPagerAdapter {

    public TabsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DetailsFragment();
            case 1:
                return new EventFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
