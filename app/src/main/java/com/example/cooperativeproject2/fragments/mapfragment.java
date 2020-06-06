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

import com.example.cooperativeproject2.R;
import com.example.cooperativeproject2.models.Task;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    private JSONObject jsonObject1;
    private JSONObject jsonObject2;
    private String phone;
    public String place_id;
    public static mapfragment context;
    private FragmentActivity mContext;
    private Boolean startcheck = true;
    private EditText editText;
    private LatLng findLatLng = null;
    private Boolean findCheck = false;      //false면 현재위치에서 검색, true면 검색한 곳에서 검색
    private MapView mapView = null;
    private GoogleMap mMap;
    private Marker currentMarker = null;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;
    private Geocoder geocoder;
    private View mLayout;
    private int placefirst = 0;     //앱 실행시 초기값이 되는데
    private int placesecond = 1;    //해결하려면 저장된 string을 여기로 가져와야지 아니면 integer도 받든가
    private int placethird = 2;
    private int placefourth = 3;
    private int placefifth = 4;
    private int checknum = -1;
    private int range = 500;  //범위다
    private List<Marker> previous_marker;
    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private boolean needRequest = false;

    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    private String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소
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
        context = this;     //placeid 다른 class에서 사용하기 위해
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
        //Layout을 inflate함
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
        //Fragment에서의 onCreateView를 마치고, Activity에서 onCreate()가 호출되고 나서 호출됨
        //Activity와 Fragment의 뷰가 모두 생성된 상태로, view를 변경하는 작업 가능
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        //액티비티가 처음 생성될 때 실행되는 함수
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
        if(inputText1.equals(""));            //sharedpreference가 비어있으면 초기 설정값이 출력
        else
            button1.setText(inputText1);
        button1.setOnClickListener(v -> {
            Log.d(TAG, "1번눌림");
            if(!findCheck)
                showFirstInformation(currentPosition);  //현재 위치 기반으로 뜨게함
            else
                showFirstInformation(findLatLng);  //검색한 곳이 뜨게함
        });
        button1.setOnLongClickListener(v -> {
            checknum = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            selectedItem.add(itemsInKor[0]);
            builder.setTitle("1번");
            builder.setSingleChoiceItems(R.array.array_Kor,
                    0, (dialog, which) -> {
                        selectedItem.clear();
                        selectedItem.add(itemsInKor[which]);
                        checknum = which;         //문제점 ok를 누르지 않아도 placefirst에 저장이되어버림
                    });
            builder.setPositiveButton("OK", (dialog, pos) -> {
                Toast toast = Toast.makeText(mContext.getApplicationContext(), "선택된 항목 : " + selectedItem.get(0), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                //placefirst = itemsInKor[which];
                toast.show();
                if(checknum != -1)
                    placefirst = checknum;
                button1.setText(selectedItem.get(0));
                SharedPreferences sharedPreferences= mContext.getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정
                SharedPreferences.Editor editor= sharedPreferences.edit(); //sharedPreferences를 제어할 editor를 선언
                editor.putString("inputText1",selectedItem.get(0));
                editor.putInt("inputNum1",placefirst);
                editor.apply();
                Toast.makeText(mContext,"저장",Toast.LENGTH_SHORT).show();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        });

        final Button button2 = (Button)mContext.findViewById(R.id.btn2);
        if(inputText2.equals(""));        //초기 설정값
        else
            button2.setText(inputText2);
        button2.setOnClickListener(v -> {
            Log.d(TAG, "2번눌림");
            if(!findCheck)
                showSecondInformation(currentPosition);  //현재 위치 기반으로 뜨게함
            else
                showSecondInformation(findLatLng);  //검색한 곳이 뜨게함
        });
        button2.setOnLongClickListener(v -> {
            checknum = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            selectedItem.add(itemsInKor[0]);
            builder.setTitle("2번");
            builder.setSingleChoiceItems(R.array.array_Kor,
                    0, (dialog, which) -> {
                        selectedItem.clear();
                        selectedItem.add(itemsInKor[which]);
                        checknum = which;
                    });
            builder.setPositiveButton("OK", (dialog, pos) -> {
                Toast toast = Toast.makeText(mContext.getApplicationContext(), "선택된 항목 : " + selectedItem.get(0), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                if(checknum != -1)
                    placesecond = checknum;
                button2.setText(selectedItem.get(0));
                SharedPreferences sharedPreferences= mContext.getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정
                SharedPreferences.Editor editor= sharedPreferences.edit(); //sharedPreferences를 제어할 editor를 선언
                editor.putInt("inputNum2",placesecond);
                editor.putString("inputText2",selectedItem.get(0));
                editor.apply();
                Toast.makeText(mContext,"저장",Toast.LENGTH_SHORT).show();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        });

        Button button3 = (Button)mContext.findViewById(R.id.btn3);
        if(inputText3.equals(""));            //초기설정값
        else
            button3.setText(inputText3);
        button3.setOnClickListener(v -> {
            Log.d(TAG, "3번눌림");
            if(!findCheck)
                showThirdInformation(currentPosition);  //현재 위치 기반으로 뜨게함
            else
                showThirdInformation(findLatLng);  //검색한 곳이 뜨게함
        });

        button3.setOnLongClickListener(v -> {
            checknum = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            selectedItem.add(itemsInKor[0]);
            builder.setTitle("3번");
            builder.setSingleChoiceItems(R.array.array_Kor,
                    0, (dialog, which) -> {
                        selectedItem.clear();
                        selectedItem.add(itemsInKor[which]);
                        checknum = which;
                    });
            builder.setPositiveButton("OK", (dialog, pos) -> {
                Toast toast = Toast.makeText(mContext.getApplicationContext(), "선택된 항목 : " + selectedItem.get(0), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                if(checknum != -1)      //버튼 안누르고 ok누르는 거 방지
                    placethird = checknum;
                button3.setText(selectedItem.get(0));
                SharedPreferences sharedPreferences= mContext.getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정
                SharedPreferences.Editor editor= sharedPreferences.edit(); //sharedPreferences를 제어할 editor를 선언
                editor.putInt("inputNum3",placethird);
                editor.putString("inputText3",selectedItem.get(0));
                editor.apply();
                Toast.makeText(mContext,"저장",Toast.LENGTH_SHORT).show();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        });

        Button button4 = (Button)mContext.findViewById(R.id.btn4);
        if(inputText4.equals(""));            //초기설정값
        else
            button4.setText(inputText4);
        button4.setOnClickListener(v -> {
            Log.d(TAG, "4번눌림");
            if(!findCheck)
                showFourthInformation(currentPosition);  //현재 위치 기반으로 뜨게함
            else
                showFourthInformation(findLatLng);  //검색한 곳이 뜨게함
        });

        button4.setOnLongClickListener(v -> {
            checknum = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            selectedItem.add(itemsInKor[0]);
            builder.setTitle("4번");
            builder.setSingleChoiceItems(R.array.array_Kor,
                    0, (dialog, which) -> {
                        selectedItem.clear();
                        selectedItem.add(itemsInKor[which]);
                        checknum = which;
                    });
            builder.setPositiveButton("OK", (dialog, pos) -> {
                Toast toast = Toast.makeText(mContext.getApplicationContext(), "선택된 항목 : " + selectedItem.get(0), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                if(checknum != -1)
                    placefourth = checknum;
                button4.setText(selectedItem.get(0));
                SharedPreferences sharedPreferences= mContext.getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정
                SharedPreferences.Editor editor= sharedPreferences.edit(); //sharedPreferences를 제어할 editor를 선언
                editor.putString("inputText4",selectedItem.get(0));
                editor.putInt("inputNum4",placefourth);
                editor.apply();
                Toast.makeText(mContext,"저장",Toast.LENGTH_SHORT).show();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        });

        Button button5 = (Button)mContext.findViewById(R.id.btn5);
        if(inputText5.equals(""));            //초기설정값
        else
            button5.setText(inputText5);

        button5.setOnClickListener(v -> Log.d(TAG, "5번눌림"));

        //onclick으로 바꾸기
        button5.setOnLongClickListener(v -> {
            checknum = -1;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            selectedItem.add(distance[0]);      //왜하는지 모르겠음
            builder.setTitle("5번");
            builder.setSingleChoiceItems(R.array.distance,
                    0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedItem.clear();
                            selectedItem.add(distance[which]);
                            checknum = which;     //문제가 ok를 눌렀을 때 변경이 되어야되는데 그게 아님
                        }
                    });
            builder.setPositiveButton("OK", (dialog, pos) -> {
                Toast toast = Toast.makeText(mContext.getApplicationContext(), "선택된 항목 : " + selectedItem.get(0), Toast.LENGTH_LONG);
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
                SharedPreferences sharedPreferences= mContext.getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정
                SharedPreferences.Editor editor= sharedPreferences.edit(); //sharedPreferences를 제어할 editor를 선언
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

        //FusedLocationProviderClient 객체 생성
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

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();


        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            startLocationUpdates(); // 3. 위치 업데이트 시작


        }else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions( mContext, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions( mContext, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String tag = marker.getTag().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(tag));//...문제점 함수만들기 마지막번호만 하는 것이 아닌
                startActivity(intent);
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
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet + markerTitle);


                //현재 위치에 마커 생성하고 이동
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

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
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

        //지오코더... GPS를 주소로 변환
        geocoder = new Geocoder(mContext);

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
            Log.d(TAG, "주소가 왜 미발견 : " + addresses );
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(mContext, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(mContext, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(mContext, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

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


     //마커x   currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        if(startcheck){
            mMap.moveCamera(cameraUpdate);
            startcheck=false;       //이 코드가 앱을 처음 시작했을 때 현재위치로 옮겨줌
        }
    }


    private void setDefaultLocation() {


        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


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


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
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
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(mContext, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", view -> {

                                //   finish();
                            }).show();

                }else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", view -> {

                                //    finish();
                            }).show();
                }
            }

        }
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", (dialog, id) -> dialog.cancel());
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
            String str=editText.getText().toString();       //검색창에 아무것도 입력하지 않으면 edittext값이 없어서 튕기는거임
            List<Address> addressList = null;
            try {
                // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                addressList = geocoder.getFromLocationName(
                        str, // 주소
                        1); // 최대 검색 결과 개수
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            // 좌표(위도, 경도) 생성
            LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
            //음식점,놀거리 찾기위한 위도 저장
            findLatLng = latLng;
            findCheck = true;       //검색한 위치 근처의 음식점만 보이도록
            // 마커 생성
            mMap.addMarker(new MarkerOptions().position(latLng).title(str));
            // 해당 좌표로 화면 줌
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            //when error
            //initialize status
            Status status = Autocomplete.getStatusFromIntent(data);
            //Display toast
            Log.d(TAG, "입력안했을때냐");
            Toast.makeText(mContext.getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }        // 좌표(위도, 경도) 생성


        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");


                        needRequest = true;

                        return;
                    }
                }

                break;
        }
    }


    //근처찾기 method
    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {
        Log.d(TAG, "여기까지 오긴하냐");
        getActivity().runOnUiThread(() -> {

            Log.d(TAG, "z");

            for (noman.googleplaces.Place place : places) {

                LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());

                String markerSnippet = getCurrentAddress(latLng);

                place_id = place.getPlaceId();
                String str = "";
                try{
                    Log.d(TAG,"으어어");
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

            //중복마커 제거
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
        mMap.clear();//지도 클리어
        Log.d(TAG, "1번실행"+placefirst);
        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어
        if(placefirst == 0){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.RESTAURANT) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "음식점", Toast.LENGTH_SHORT).show();

        } else if(placefirst == 1){
            Log.d(TAG, "1번실행"+placefirst);

            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.CAFE) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "카페", Toast.LENGTH_SHORT).show();
        } else if(placefirst == 2){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.BAR) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "바", Toast.LENGTH_SHORT).show();
        } else if(placefirst == 3){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.HOSPITAL) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "병원", Toast.LENGTH_SHORT).show();
        } else if(placefirst == 4){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.BANK) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "은행", Toast.LENGTH_SHORT).show();
        } else if(placefirst == 5){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.TAXI_STAND) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "택시정류장", Toast.LENGTH_SHORT).show();
        }


    }
    private void showSecondInformation(LatLng location)
    {
        mMap.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        Log.d(TAG, "2번실행");
        if(placesecond == 0){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.RESTAURANT) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "음식점", Toast.LENGTH_SHORT).show();
        }
        else if(placesecond == 1){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.CAFE) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "카페", Toast.LENGTH_SHORT).show();
        }
        else if(placesecond == 2){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.BAR) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "바", Toast.LENGTH_SHORT).show();
        }
        else if(placesecond == 3){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.HOSPITAL) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "병원", Toast.LENGTH_SHORT).show();
        } else if(placesecond == 4){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.BANK) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "은행", Toast.LENGTH_SHORT).show();
        } else if(placesecond == 5){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.TAXI_STAND) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "택시정류장", Toast.LENGTH_SHORT).show();
        }
    }
    private void showThirdInformation(LatLng location)
    {
        mMap.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        Log.d(TAG, "3번실행");
        if(placethird == 0){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.RESTAURANT) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "음식점", Toast.LENGTH_SHORT).show();
        }
        else if(placethird == 1){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.CAFE) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "카페", Toast.LENGTH_SHORT).show();
        }
        else if(placethird == 2){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.BAR) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "바", Toast.LENGTH_SHORT).show();
        }
        else if(placethird == 3){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.HOSPITAL) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "병원", Toast.LENGTH_SHORT).show();
        } else if(placethird == 4){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.BANK) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "은행", Toast.LENGTH_SHORT).show();
        } else if(placethird == 5){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.TAXI_STAND) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "택시정류장", Toast.LENGTH_SHORT).show();
        }
    }
    private void showFourthInformation(LatLng location)
    {
        mMap.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        Log.d(TAG, "4번실행");
        if(placefourth == 0){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.RESTAURANT) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "음식점", Toast.LENGTH_SHORT).show();
        }
        else if(placefourth == 1){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.CAFE) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "카페", Toast.LENGTH_SHORT).show();
        }
        else if(placefourth == 2){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.BAR) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "바", Toast.LENGTH_SHORT).show();
        }
        else if(placefourth == 3){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.HOSPITAL) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "병원", Toast.LENGTH_SHORT).show();
        } else if(placefourth == 4){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.BANK) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "은행", Toast.LENGTH_SHORT).show();
        } else if(placefourth == 5){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(range) //500 미터 내에서 검색
                    .type(PlaceType.TAXI_STAND) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "택시정류장", Toast.LENGTH_SHORT).show();
        }
    }


    private void showFifthInformation(LatLng location)
    {
        mMap.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        Log.d(TAG, "5번실행");
        if(placefifth == 0){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(500) //500 미터 내에서 검색
                    .type(PlaceType.RESTAURANT) //
                    .build()
                    .execute();
        }
        else if(placefifth == 1){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(500) //500 미터 내에서 검색
                    .type(PlaceType.CAFE) //
                    .build()
                    .execute();
        }
        else if(placefifth == 2){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(500) //500 미터 내에서 검색
                    .type(PlaceType.BAR) //
                    .build()
                    .execute();
        }
        else if(placefifth == 3){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(500) //500 미터 내에서 검색
                    .type(PlaceType.HOSPITAL) //
                    .build()
                    .execute();
        } else if(placefifth == 4){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(500) //500 미터 내에서 검색
                    .type(PlaceType.BANK) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "은행", Toast.LENGTH_SHORT).show();
        } else if(placefifth == 5){
            new NRPlaces.Builder()
                    .listener(mapfragment.this)
                    .key("AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(500) //500 미터 내에서 검색
                    .type(PlaceType.TAXI_STAND) //
                    .build()
                    .execute();
            Toast.makeText(mContext, "택시정류장", Toast.LENGTH_SHORT).show();
        }
    }
    //여기까지 근처위치 찾기
}