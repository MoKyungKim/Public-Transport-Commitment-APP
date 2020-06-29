package com.example.cooperativeproject2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.fragment.app.FragmentActivity;

public class routeActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String[] SX;
    String[] SY;
    String slat;
    String slng;
    String elat;
    String elng;
    String[] EX;
    String[] EY;
    String[] start;
    String[] end;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityroute);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        SX = intent.getStringArrayExtra("SX");
        SY = intent.getStringArrayExtra("SY");
        EX = intent.getStringArrayExtra("EX");
        EY = intent.getStringArrayExtra("EY");
        start = intent.getStringArrayExtra("startname");
        end = intent.getStringArrayExtra("endname");
        slat = intent.getStringExtra("SLat");
        slng = intent.getStringExtra("SLng");
        elat = intent.getStringExtra("ELat");
        elng = intent.getStringExtra("ELng");
        Toast.makeText(this, SX+"", Toast.LENGTH_SHORT).show();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        MarkerOptions marker = new MarkerOptions();

        LatLng startpoint = new LatLng(Double.parseDouble(slat), Double.parseDouble(slng));
        mMap.addMarker(marker.position(startpoint).title("출발위치"));


        LatLng endpoint = new LatLng(Double.parseDouble(elat), Double.parseDouble(elng));
        mMap.addMarker(marker.position(endpoint).title("도착위치"));

        mMap.addMarker(marker.position(new LatLng(Double.parseDouble(SX[1]), Double.parseDouble(SY[1]))).title("승차정류장"));

        mMap.addMarker(marker.position( new LatLng(Double.parseDouble(EX[1]), Double.parseDouble(EY[1]))).title("하차정류장"));


        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(startpoint, 15);
        mMap.moveCamera(cameraUpdate);

    }
}
