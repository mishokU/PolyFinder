package com.example.polyfinder;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BottomFragmentsAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> listOfFragments = new ArrayList<>();
    private List<String> listOfNames = new ArrayList<>();

    public BottomFragmentsAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment){
        listOfFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return listOfFragments.get(position);
    }

    @Override
    public int getCount() {
        return listOfFragments.size();
    }
}
