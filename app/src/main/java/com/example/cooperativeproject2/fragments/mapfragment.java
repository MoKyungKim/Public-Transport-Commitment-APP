package com.example.cooperativeproject2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cooperativeproject2.R;

import net.daum.mf.map.api.MapView;

public class mapfragment extends Fragment {

    private static mapfragment INSTANCE = null;

    View view;
    MapView mapView;

    public mapfragment(){
    }

    public static mapfragment getINSTANCE(){
        if(INSTANCE == null)
            INSTANCE = new mapfragment();
        return INSTANCE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mapfragment,container,false);

        mapView = new MapView(getActivity());
        ViewGroup mapViewContainer = view.findViewById(R.id.mapsView);
        mapViewContainer.addView(mapView);

        return view;
    }

}
