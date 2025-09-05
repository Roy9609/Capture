package com.roy.capture.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class CaptureMainPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>() ;
    public CaptureMainPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public List<Fragment> getFragments(){
        return fragments;
    }
    public void setData(List<Fragment> data){
        fragments.clear();
        fragments.addAll(data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (fragments != null && fragments.size() > 0){
            return fragments.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (fragments != null && fragments.size() > 0){
            return fragments.size();
        }
        return 0;
    }
}
