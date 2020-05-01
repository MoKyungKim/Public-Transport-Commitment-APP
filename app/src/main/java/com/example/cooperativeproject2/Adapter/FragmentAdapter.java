package com.example.cooperativeproject2.Adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cooperativeproject2.fragments.calfragment;
import com.example.cooperativeproject2.fragments.findfragment;
import com.example.cooperativeproject2.fragments.mapfragment;

public class FragmentAdapter extends FragmentPagerAdapter {

    Context context;

    public FragmentAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position){
        if(position == 0)
            return calfragment.getINSTANCE();
        else if(position == 1)
            return findfragment.getINSTANCE();
        else if(position == 2)
            return mapfragment.getINSTANCE();
        else
            return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "캘린더";

            case 1:
                return "길찾기";

            case 2:
                return "지도";
        }

        return "";
    }
}