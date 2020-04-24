package com.example.cooperativeproject2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cooperativeproject2.R;

public class findfragment extends Fragment {

    private static findfragment INSTANCE = null;

    View view;

    public findfragment(){
    }

    public static findfragment getINSTANCE(){
        if(INSTANCE == null)
            INSTANCE = new findfragment();
        return INSTANCE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.findfragment,container, false);
        return view;
    }
}
