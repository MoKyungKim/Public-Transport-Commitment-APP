package com.example.cooperativeproject2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cooperativeproject2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class mapfragment extends Fragment implements OnMapReadyCallback {

    private static mapfragment INSTANCE = null;

    View view;

    GoogleMap map;
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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapsView);

        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;

        LatLng hwajeon = new LatLng(37.6032842, 126.868761);
        map.addMarker(new MarkerOptions().position(hwajeon).title("화전역"));
        map.moveCamera(CameraUpdateFactory.newLatLng(hwajeon));
        map.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

    }
}
