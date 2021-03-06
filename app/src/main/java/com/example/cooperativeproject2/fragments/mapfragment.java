package com.example.cooperativeproject2.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.cooperativeproject2.MainActivity;
import com.example.cooperativeproject2.R;
import com.example.cooperativeproject2.blogActivity;
import com.example.cooperativeproject2.models.Task;
import com.example.cooperativeproject2.models.Task1;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class mapfragment extends Fragment implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        PlacesListener {

    MainActivity mainActivity;
    private JSONObject jsonObject1;
    private JSONObject jsonObject2;
    private String phone;
    Double late;
    Double lnge;
    String markerName;
    int startendcheck = 0;
    public String place_id;
    public String addressName;
    public static mapfragment context;
    private FragmentActivity mContext;
    private Boolean startcheck = true;
    private EditText editText;
    private LatLng findLatLng = null;
    private Boolean findCheck = false;      //false??? ?????????????????? ??????, true??? ????????? ????????? ??????
    private MapView mapView = null;
    private GoogleMap mMap;
    private Marker currentMarker = null;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;
    private Geocoder geocoder;
    private View mLayout;
    private int placefirst = 0;     //??? ????????? ???????????? ?????????
    private int placesecond = 1;    //??????????????? ????????? string??? ????????? ??????????????? ????????? integer??? ?????????
    private int placethird = 2;
    private int placefourth = 3;
    private int placefifth = 4;
    private int checknum = -1;
    private int range = 500;  //?????????
    private List<Marker> previous_marker;
    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1???
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5???

    // onRequestPermissionsResult?????? ????????? ???????????? ActivityCompat.requestPermissions??? ????????? ????????? ????????? ???????????? ?????? ???????????????.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private boolean needRequest = false;

    // ?????? ???????????? ?????? ????????? ???????????? ???????????????.
    private String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // ?????? ?????????
    private String inputText1;
    private String inputText2;
    private String inputText3;
    private String inputText4;
    private String inputText5;

    private int inputNum1;
    private int inputNum2;
    private int inputNum3;
    private int inputNum4;
    private int inputNum5;

    private Location mCurrentLocatiion;
    private LatLng currentPosition;

    public mapfragment(){
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach");
        mContext = (FragmentActivity)context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        findCheck = false;
        context = this;     //placeid ?????? class?????? ???????????? ??????
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("test",MODE_PRIVATE);
        inputText1 = sharedPreferences.getString("inputText1","");
        inputText2 = sharedPreferences.getString("inputText2","");
        inputText3 = sharedPreferences.getString("inputText3","");
        inputText4 = sharedPreferences.getString("inputText4","");
        inputText5 = sharedPreferences.getString("inputText5","");

        inputNum1 = sharedPreferences.getInt("inputNum1", -1);
        inputNum2 = sharedPreferences.getInt("inputNum2", -1);
        inputNum3 = sharedPreferences.getInt("inputNum3", -1);
        inputNum4 = sharedPreferences.getInt("inputNum4", -1);
        inputNum5 = sharedPreferences.getInt("inputNum5", -1);
        placefirst = inputNum1;
        placesecond = inputNum2;
        placethird = inputNum3;
        placefourth = inputNum4;
        placefifth = inputNum5;
        if(placefifth == 0)
            range = 500;
        else if(placefifth == 1)
            range = 1000;
        else if(placefifth == 2)
            range = 1500;
        else if(placefifth == 3)
            range = 2000;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Layout??? inflate???
        Log.d(TAG, "onCreateView");
        mContext.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        View view = inflater.inflate(R.layout.mapfragment,container,false);
        editText = view.findViewById(R.id.edit_text);
        mapView = view.findViewById(R.id.map);
        mLayout = view.findViewById(R.id.layout_main);
        if(mapView != null){
            mapView.onCreate(savedInstanceState);
        }
        mapView.getMapAsync(this);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        //Fragment????????? onCreateView??? ?????????, Activity?????? onCreate()??? ???????????? ?????? ?????????
        //Activity??? Fragment??? ?????? ?????? ????????? ?????????, view??? ???????????? ?????? ??????
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        //??????????????? ?????? ????????? ??? ???????????? ??????
        MapsInitializer.initialize(mContext);

        final String[] itemsInKor = getResources().getStringArray(R.array.array_Kor);
        final String[] distance = getResources().getStringArray(R.array.distance);
        final ArrayList<String> selectedItem  = new ArrayList<String>();

        //Initialize places
        Places.initialize(mContext.getApplicationContext(),"AIzaSyBGN9HuTUWakZjy19FTkGPw4KZML3sbJfc");

        //Set EditText non focusable
        editText.setFocusable(false);
        editText.setOnClickListener(v -> {
            //Initialize place field list
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                    Place.Field.LAT_LNG,Place.Field.NAME);
            //Create intent
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.
                    OVERLAY,fieldList).build(mContext);
            //Start activity result
            startActivityForResult(intent,100);
        });


        previous_marker = new ArrayList<Marker>();

        final Button button1 = (Button)mContext.findViewById(R.id.btn1);
        if(inputText1.equals(""));            //sharedpreference??? ??????????????? ?????? ???????????? ??????
        else
            button1.setText(inputText1);
        button1.setOnClickListener(v -> {
            Log.d(TAG, "1?????????");
            if(!findCheck)
                showFirstInformation(currentPosition);  //?????? ?????? ???????????? ?????????
            else
                showFirstInformation(findLatLng);  //????????? ?????? ?????????
        });
        button1.setOnLongClickListener(v -> {
            checknum = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            selectedItem.add(itemsInKor[0]);
            builder.setTitle("1???");
            builder.setSingleChoiceItems(R.array.array_Kor,
                    placefirst, (dialog, which) -> {
                        selectedItem.clear();
                        selectedItem.add(itemsInKor[which]);
                        checknum = which;         //????????? ok??? ????????? ????????? placefirst??? ?????????????????????
                    });
            builder.setPositiveButton("OK", (dialog, pos) -> {
                Toast toast = Toast.makeText(mContext.getApplicationContext(), "????????? ?????? : " + selectedItem.get(0), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                //placefirst = itemsInKor[which];
                toast.show();
                if(checknum != -1)
                    placefirst = checknum;
                button1.setText(selectedItem.get(0));
                SharedPreferences sharedPreferences= mContext.getSharedPreferences("test", MODE_PRIVATE);    // test ????????? ???????????? ??????
                SharedPreferences.Editor editor= sharedPreferences.edit(); //sharedPreferences??? ????????? editor??? ??????
                editor.putString("inputText1",selectedItem.get(0));
                editor.putInt("inputNum1",placefirst);
                editor.apply();
                Toast.makeText(mContext,"??????",Toast.LENGTH_SHORT).show();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        });

        final Button button2 = (Button)mContext.findViewById(R.id.btn2);
        if(inputText2.equals(""));        //?????? ?????????
        else
            button2.setText(inputText2);
        button2.setOnClickListener(v -> {
            Log.d(TAG, "2?????????");
            if(!findCheck)
                showSecondInformation(currentPosition);  //?????? ?????? ???????????? ?????????
            else
                showSecondInformation(findLatLng);  //????????? ?????? ?????????
        });
        button2.setOnLongClickListener(v -> {
            checknum = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            selectedItem.add(itemsInKor[0]);
            builder.setTitle("2???");
            builder.setSingleChoiceItems(R.array.array_Kor,
                    placesecond, (dialog, which) -> {
                        selectedItem.clear();
                        selectedItem.add(itemsInKor[which]);
                        checknum = which;
                    });
            builder.setPositiveButton("OK", (dialog, pos) -> {
                Toast toast = Toast.makeText(mContext.getApplicationContext(), "????????? ?????? : " + selectedItem.get(0), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                if(checknum != -1)
                    placesecond = checknum;
                button2.setText(selectedItem.get(0));
                SharedPreferences sharedPreferences= mContext.getSharedPreferences("test", MODE_PRIVATE);    // test ????????? ???????????? ??????
                SharedPreferences.Editor editor= sharedPreferences.edit(); //sharedPreferences??? ????????? editor??? ??????
                editor.putInt("inputNum2",placesecond);
                editor.putString("inputText2",selectedItem.get(0));
                editor.apply();
                Toast.makeText(mContext,"??????",Toast.LENGTH_SHORT).show();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        });

        Button button3 = (Button)mContext.findViewById(R.id.btn3);
        if(inputText3.equals(""));            //???????????????
        else
            button3.setText(inputText3);
        button3.setOnClickListener(v -> {
            Log.d(TAG, "3?????????");
            if(!findCheck)
                showThirdInformation(currentPosition);  //?????? ?????? ???????????? ?????????
            else
                showThirdInformation(findLatLng);  //????????? ?????? ?????????
        });

        button3.setOnLongClickListener(v -> {
            checknum = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            selectedItem.add(itemsInKor[0]);
            builder.setTitle("3???");
            builder.setSingleChoiceItems(R.array.array_Kor,
                    placethird, (dialog, which) -> {
                        selectedItem.clear();
                        selectedItem.add(itemsInKor[which]);
                        checknum = which;
                    });
            builder.setPositiveButton("OK", (dialog, pos) -> {
                Toast toast = Toast.makeText(mContext.getApplicationContext(), "????????? ?????? : " + selectedItem.get(0), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                if(checknum != -1)      //?????? ???????????? ok????????? ??? ??????
                    placethird = checknum;
                button3.setText(selectedItem.get(0));
                SharedPreferences sharedPreferences= mContext.getSharedPreferences("test", MODE_PRIVATE);    // test ????????? ???????????? ??????
                SharedPreferences.Editor editor= sharedPreferences.edit(); //sharedPreferences??? ????????? editor??? ??????
                editor.putInt("inputNum3",placethird);
                editor.putString("inputText3",selectedItem.get(0));
                editor.apply();
                Toast.makeText(mContext,"??????",Toast.LENGTH_SHORT).show();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        });

        Button button4 = (Button)mContext.findViewById(R.id.btn4);
        if(inputText4.equals(""));            //???????????????
        else
            button4.setText(inputText4);
        button4.setOnClickListener(v -> {
            Log.d(TAG, "4?????????");
            if(!findCheck)
                showFourthInformation(currentPosition);  //?????? ?????? ???????????? ?????????
            else
                showFourthInformation(findLatLng);  //????????? ?????? ?????????
        });

        button4.setOnLongClickListener(v -> {
            checknum = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            selectedItem.add(itemsInKor[0]);
            builder.setTitle("4???");
            builder.setSingleChoiceItems(R.array.array_Kor,
                    placefourth, (dialog, which) -> {
                        selectedItem.clear();
                        selectedItem.add(itemsInKor[which]);
                        checknum = which;
                    });
            builder.setPositiveButton("OK", (dialog, pos) -> {
                Toast toast = Toast.makeText(mContext.getApplicationContext(), "????????? ?????? : " + selectedItem.get(0), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                if(checknum != -1)
                    placefourth = checknum;
                button4.setText(selectedItem.get(0));
                SharedPreferences sharedPreferences= mContext.getSharedPreferences("test", MODE_PRIVATE);    // test ????????? ???????????? ??????
                SharedPreferences.Editor editor= sharedPreferences.edit(); //sharedPreferences??? ????????? editor??? ??????
                editor.putString("inputText4",selectedItem.get(0));
                editor.putInt("inputNum4",placefourth);
                editor.apply();
                Toast.makeText(mContext,"??????",Toast.LENGTH_SHORT).show();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        });

        Button button5 = (Button)mContext.findViewById(R.id.btn5);
        if(inputText5.equals(""));            //???????????????
        else
            button5.setText(inputText5);

        button5.setOnClickListener(v -> Log.d(TAG, "5?????????"));

        //onclick?????? ?????????
        button5.setOnLongClickListener(v -> {
            checknum = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            selectedItem.add(distance[0]);      //???????????? ????????????
            builder.setTitle("5???");
            builder.setSingleChoiceItems(R.array.distance,
                    placefifth, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedItem.clear();
                            selectedItem.add(distance[which]);
                            checknum = which;     //????????? ok??? ????????? ??? ????????? ?????????????????? ?????? ??????
                        }
                    });
            builder.setPositiveButton("OK", (dialog, pos) -> {
                Toast toast = Toast.makeText(mContext.getApplicationContext(), "????????? ?????? : " + selectedItem.get(0), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                if(checknum != -1)
                    placefifth = checknum;
                if(placefifth == 0)
                    range = 500;
                else if(placefifth == 1)
                    range = 1000;
                else if(placefifth == 2)
                    range = 1500;
                else if(placefifth == 3)
                    range = 2000;
                button5.setText(selectedItem.get(0));
                SharedPreferences sharedPreferences= mContext.getSharedPreferences("test", MODE_PRIVATE);    // test ????????? ???????????? ??????
                SharedPreferences.Editor editor= sharedPreferences.edit(); //sharedPreferences??? ????????? editor??? ??????
                editor.putString("inputText5",selectedItem.get(0));
                editor.putInt("inputNum5",placefifth);
                editor.apply();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        });




        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        //FusedLocationProviderClient ?????? ??????
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady :");

        mMap = googleMap;

        //????????? ????????? ?????? ??????????????? GPS ?????? ?????? ???????????? ???????????????
        //????????? ??????????????? ????????? ??????
        setDefaultLocation();


        //????????? ????????? ??????
        // 1. ?????? ???????????? ????????? ????????? ???????????????.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {

            // 2. ?????? ???????????? ????????? ?????????
            // ( ??????????????? 6.0 ?????? ????????? ????????? ???????????? ???????????? ????????? ?????? ????????? ?????? ???????????????.)


            startLocationUpdates(); // 3. ?????? ???????????? ??????


        }else {  //2. ????????? ????????? ????????? ?????? ????????? ????????? ????????? ???????????????. 2?????? ??????(3-1, 4-1)??? ????????????.

            // 3-1. ???????????? ????????? ????????? ??? ?????? ?????? ????????????
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, REQUIRED_PERMISSIONS[0])) {

                // 3-2. ????????? ???????????? ?????? ?????????????????? ???????????? ????????? ????????? ???????????? ????????? ????????????.
                Snackbar.make(mLayout, "??? ?????? ??????????????? ?????? ?????? ????????? ???????????????.",
                        Snackbar.LENGTH_INDEFINITE).setAction("??????", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. ??????????????? ????????? ????????? ?????????. ?????? ????????? onRequestPermissionResult?????? ???????????????.
                        ActivityCompat.requestPermissions( mContext, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. ???????????? ????????? ????????? ??? ?????? ?????? ???????????? ????????? ????????? ?????? ?????????.
                // ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions( mContext, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("??????");
                builder.setItems(R.array.startend, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LatLng latLng = marker.getPosition();
                        late = latLng.latitude;
                        lnge = latLng.longitude;
                        markerName = marker.getTitle();
                        if(which==0){       //???????????? ???????????? ?????? ???????????? find??? ???????????? ?????? ????????? ??? ??????
                            Toast.makeText(mContext.getApplicationContext(),"????????? >> "+"lat:"+late+"lng:"+lnge,Toast.LENGTH_LONG).show();
                            startendcheck = 1;
                           // findfragment.setPoint();
                        }
                        else if(which==1){  //???????????? ??????
                            Toast.makeText(mContext.getApplicationContext(),"????????? >> "+"lat:"+late+"lng:"+lnge,Toast.LENGTH_LONG).show();
                            startendcheck = 2;
                            //findfragment.setPoint();
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });

        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(final Marker marker) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("??????");
                builder.setItems(R.array.markermenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){   //????????????
                            String tag = marker.getTag().toString();
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse(tag));
                            startActivity(intent);
                        }
                        else if(which==1){      //?????????
                            String search = marker.getTitle();
                            Intent intent = new Intent((MainActivity)getActivity(), blogActivity.class);
                            intent.putExtra("keyword",search);
                            startActivity(intent);
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationButtonClickListener(() -> {
            Log.d( TAG, "onMyLocationButtonClicked");
            findCheck = false;
            return false;
        });
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(latLng -> Log.d( TAG, "onMapClick :"));

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }




    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());


                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "??????:" + String.valueOf(location.getLatitude())
                        + " ??????:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet + markerTitle);


                //?????? ????????? ?????? ???????????? ??????
                setCurrentLocation(location, markerTitle, markerSnippet);

                mCurrentLocatiion = location;
            }


        }

    };



    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION);



            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {

                Log.d(TAG, "startLocationUpdates : ????????? ???????????? ??????");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mMap.setMyLocationEnabled(true);

        }

    }


    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");

        if (checkPermission()) {

            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mMap!=null)
                mMap.setMyLocationEnabled(true);

        }


    }


    @Override
    public void onStop() {

        super.onStop();

        if (mFusedLocationClient != null) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }




    private String getCurrentAddress(LatLng latlng) {

        //????????????... GPS??? ????????? ??????
        geocoder = new Geocoder(mContext);

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
            Log.d(TAG, "????????? ??? ????????? : " + addresses );
        } catch (IOException ioException) {
            //???????????? ??????
            Toast.makeText(mContext, "???????????? ????????? ????????????", Toast.LENGTH_LONG).show();
            return "???????????? ????????? ????????????";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(mContext, "????????? GPS ??????", Toast.LENGTH_LONG).show();
            return "????????? GPS ??????";

        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(mContext, "?????? ?????????", Toast.LENGTH_LONG).show();
            return "?????? ?????????";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0);
        }

    }


    private boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


     //??????x   currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        if(startcheck){
            mMap.moveCamera(cameraUpdate);
            startcheck=false;       //??? ????????? ?????? ?????? ???????????? ??? ??????????????? ?????????
        }
    }


    private void setDefaultLocation() {


        //????????? ??????, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "???????????? ????????? ??? ??????";
        String markerSnippet = "?????? ???????????? GPS ?????? ?????? ???????????????";


        //if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);

    }


    //??????????????? ????????? ????????? ????????? ?????? ????????????
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            return true;
        }

        return false;

    }



    /*
     * ActivityCompat.requestPermissions??? ????????? ????????? ????????? ????????? ???????????? ??????????????????.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // ?????? ????????? PERMISSIONS_REQUEST_CODE ??????, ????????? ????????? ???????????? ??????????????????

            boolean check_result = true;


            // ?????? ???????????? ??????????????? ???????????????.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                // ???????????? ??????????????? ?????? ??????????????? ???????????????.
                startLocationUpdates();
            }
            else {
                // ????????? ???????????? ????????? ?????? ????????? ??? ?????? ????????? ??????????????? ?????? ???????????????.2 ?????? ????????? ????????????.

                if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(mContext, REQUIRED_PERMISSIONS[1])) {


                    // ???????????? ????????? ????????? ???????????? ?????? ?????? ???????????? ????????? ???????????? ?????? ????????? ??? ????????????.
                    Snackbar.make(mLayout, "???????????? ?????????????????????. ?????? ?????? ???????????? ???????????? ??????????????????. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("??????", view -> {

                                //   finish();
                            }).show();

                }else {


                    // "?????? ?????? ??????"??? ???????????? ???????????? ????????? ????????? ???????????? ??????(??? ??????)?????? ???????????? ???????????? ?????? ????????? ??? ????????????.
                    Snackbar.make(mLayout, "???????????? ?????????????????????. ??????(??? ??????)?????? ???????????? ???????????? ?????????. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("??????", view -> {

                                //    finish();
                            }).show();
                }
            }

        }
    }


    //??????????????? GPS ???????????? ?????? ????????????
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("?????? ????????? ????????????");
        builder.setMessage("?????? ???????????? ???????????? ?????? ???????????? ???????????????.\n"
                + "?????? ????????? ???????????????????");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("??????", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == AutocompleteActivity.RESULT_OK){
            //When success
            //Initialize plac
            Place place = Autocomplete.getPlaceFromIntent(data);
            //Set address on EditText
            editText.setText(place.getAddress());
            String str=editText.getText().toString();       //???????????? ???????????? ???????????? ????????? edittext?????? ????????? ???????????????
            List<Address> addressList = null;
            try {
                // editText??? ????????? ?????????(??????, ??????, ?????? ???)??? ?????? ????????? ????????? ??????
                addressList = geocoder.getFromLocationName(
                        str, // ??????
                        1); // ?????? ?????? ?????? ??????
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            addressName = place.getName();
            String str1 = "";
            try {
                Log.d(TAG, "?????????");
                str1 = new Task1().execute().get();
                Log.d(TAG, str1 + "???");
                JSONArray jsonArray = new JSONObject(str1).getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap hashMap = new HashMap<>();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    place_id = jsonObject.optString("place_id");
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
            try{
                Log.d(TAG, "?????????");
                str = new Task().execute().get();
                Log.d(TAG,  str+ "???");
                jsonObject1 = new JSONObject(str);
                String result = jsonObject1.getString("result");
                jsonObject2 = new JSONObject(result);
                phone = jsonObject2.getString("formatted_phone_number");
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }


            Address address = addressList.get(0);
            // ??????(??????, ??????) ??????
            LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
            //?????????,????????? ???????????? ?????? ??????
            findLatLng = latLng;
            findCheck = true;       //????????? ?????? ????????? ???????????? ????????????

            // ?????? ??????
            previous_marker.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title(addressName).snippet(phone)).setTag("tel:"+phone);        //????????? ????????? ???????????? ????????? ???????????? parsing?????????
            // ?????? ????????? ?????? ???
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            //when error
            //initialize status
            Status status = Autocomplete.getStatusFromIntent(data);
            //Display toast
            Log.d(TAG, "?????????????????????");
            Toast.makeText(mContext.getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }        // ??????(??????, ??????) ??????


        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //???????????? GPS ?????? ???????????? ??????
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : GPS ????????? ?????????");


                        needRequest = true;

                        return;
                    }
                }

                break;
        }
    }


    //???????????? method
    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {
        Log.d(TAG, "???????????? ????????????");
        getActivity().runOnUiThread(() -> {

            Log.d(TAG, "z");

            for (noman.googleplaces.Place place : places) {

                LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());

                String markerSnippet = getCurrentAddress(latLng);

                place_id = place.getPlaceId();
                String str = "";
                try{
                    Log.d(TAG,"?????????");
                    str = new Task().execute().get();
                    Log.d(TAG,"dhodhodhdo");
                    jsonObject1 = new JSONObject(str);
                    String result = jsonObject1.getString("result");
                    jsonObject2 = new JSONObject(result);
                    phone = jsonObject2.getString("formatted_phone_number");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG,"\n"+place.getName()+"\n"+getCurrentAddress(latLng)+"\n"+phone);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(place.getName());
                markerOptions.snippet(phone);
                Marker item = mMap.addMarker(markerOptions);
                item.setTag("tel:"+phone);
                previous_marker.add(item);
            }

            //???????????? ??????
            HashSet<Marker> hashSet = new HashSet<Marker>();
            hashSet.addAll(previous_marker);
            previous_marker.clear();
            previous_marker.addAll(hashSet);
        });
    }

    @Override
    public void onPlacesFinished() {

    }

    private void showFirstInformation(LatLng location)
    {
        mMap.clear();//?????? ?????????
        Log.d(TAG, "1?????????"+placefirst);
        if (previous_marker != null)
            previous_marker.clear();//???????????? ?????? ?????????
        if(placefirst == 0){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.RESTAURANT) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "?????????", Toast.LENGTH_SHORT).show();

        } else if(placefirst == 1){
            Log.d(TAG, "1?????????"+placefirst);

            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.CAFE) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
        } else if(placefirst == 2){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.BAR) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "???", Toast.LENGTH_SHORT).show();
        } else if(placefirst == 3){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.HOSPITAL) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
        } else if(placefirst == 4){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.BANK) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
        } else if(placefirst == 5){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.TAXI_STAND) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
        }


    }
    private void showSecondInformation(LatLng location)
    {
        mMap.clear();//?????? ?????????

        if (previous_marker != null)
            previous_marker.clear();//???????????? ?????? ?????????

        Log.d(TAG, "2?????????");
        if(placesecond == 0){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.RESTAURANT) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "?????????", Toast.LENGTH_SHORT).show();
        }
        else if(placesecond == 1){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.CAFE) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
        }
        else if(placesecond == 2){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.BAR) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "???", Toast.LENGTH_SHORT).show();
        }
        else if(placesecond == 3){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.HOSPITAL) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
        } else if(placesecond == 4){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.BANK) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
        } else if(placesecond == 5){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.TAXI_STAND) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
        }
    }
    private void showThirdInformation(LatLng location)
    {
        mMap.clear();//?????? ?????????

        if (previous_marker != null)
            previous_marker.clear();//???????????? ?????? ?????????

        Log.d(TAG, "3?????????");
        if(placethird == 0){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.RESTAURANT) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "?????????", Toast.LENGTH_SHORT).show();
        }
        else if(placethird == 1){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.CAFE) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
        }
        else if(placethird == 2){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.BAR) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "???", Toast.LENGTH_SHORT).show();
        }
        else if(placethird == 3){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.HOSPITAL) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
        } else if(placethird == 4){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.BANK) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
        } else if(placethird == 5){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.TAXI_STAND) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
        }
    }
    private void showFourthInformation(LatLng location)
    {
        mMap.clear();//?????? ?????????

        if (previous_marker != null)
            previous_marker.clear();//???????????? ?????? ?????????

        Log.d(TAG, "4?????????");
        if(placefourth == 0){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.RESTAURANT) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "?????????", Toast.LENGTH_SHORT).show();
        }
        else if(placefourth == 1){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.CAFE) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
        }
        else if(placefourth == 2){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.BAR) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "???", Toast.LENGTH_SHORT).show();
        }
        else if(placefourth == 3){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.HOSPITAL) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
        } else if(placefourth == 4){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.BANK) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
        } else if(placefourth == 5){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(range) //500 ?????? ????????? ??????
                    .type(PlaceType.TAXI_STAND) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
        }
    }


    private void showFifthInformation(LatLng location)
    {
        mMap.clear();//?????? ?????????

        if (previous_marker != null)
            previous_marker.clear();//???????????? ?????? ?????????

        Log.d(TAG, "5?????????");
        if(placefifth == 0){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(500) //500 ?????? ????????? ??????
                    .type(PlaceType.RESTAURANT) //
                    .build()
                    .execute();
        }
        else if(placefifth == 1){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(500) //500 ?????? ????????? ??????
                    .type(PlaceType.CAFE) //
                    .build()
                    .execute();
        }
        else if(placefifth == 2){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(500) //500 ?????? ????????? ??????
                    .type(PlaceType.BAR) //
                    .build()
                    .execute();
        }
        else if(placefifth == 3){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(500) //500 ?????? ????????? ??????
                    .type(PlaceType.HOSPITAL) //
                    .build()
                    .execute();
        } else if(placefifth == 4){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(500) //500 ?????? ????????? ??????
                    .type(PlaceType.BANK) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
        } else if(placefifth == 5){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//?????? ??????
                    .radius(500) //500 ?????? ????????? ??????
                    .type(PlaceType.TAXI_STAND) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
        }
    }
    //???????????? ???????????? ??????
}