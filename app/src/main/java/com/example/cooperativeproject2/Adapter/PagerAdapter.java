package com.example.cooperativeproject2.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.cooperativeproject2.fragments.calfragment;
import com.example.cooperativeproject2.fragments.findfragment;
import com.example.cooperativeproject2.fragments.mapfragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new calfragment();
            case 1:
                return new mapfragment();
            case 2:
                return new findfragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // total page count
        return 3;
    }
}