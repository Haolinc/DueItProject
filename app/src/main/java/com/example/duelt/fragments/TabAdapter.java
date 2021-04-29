package com.example.duelt.fragments;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//This is an Adapter for tabLayout
public class TabAdapter extends FragmentPagerAdapter {
    //A String array to store the tab titles shown in TabLayout
    private String[] tabTitle = {"Main","MINI","MEMO","Daily Routine","Treatment Plan"};

    public TabAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    //getItem() functions is used to keep track of the position clicked for the tab and get to the corresponding Fragment
    @Override
    public Fragment getItem(int position) {
        switch (position){
            default:
                return new MainFragment();
            case 1:
                return new MiniFragment();
            case 2:
                return new MemoFragment();
            case 3:
                return new DailyFragment();
            case 4:
                return new TreatmentFragment();

        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return tabTitle[position];
    }
}
