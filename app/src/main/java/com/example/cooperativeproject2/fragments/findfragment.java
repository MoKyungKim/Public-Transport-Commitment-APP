package com.example.cooperativeproject2.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooperativeproject2.Adapter.SearchAdapter;
import com.example.cooperativeproject2.Item;
import com.example.cooperativeproject2.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

//import com.google.android.libraries.places.api.model.Place;


//위치 받아오기
//길찾기



public class findfragment extends Fragment implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, PlacesListener {


    private ArrayList<Item> items = new ArrayList<>();
     // 데이터를 넣은 리스트변수
    private RecyclerView recyclerView;          // 검색을 보여줄 리스트변수
    private EditText startSearch;        // 출발 검색어를 입력할 Input 창
    private EditText endSearch;        // 도착 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private LinearLayoutManager mLayoutManager;


    //구글맵이용
    private GoogleMap fMap;
    private MapView mapView = null;
    private Location location;
    private Geocoder geocoder;
    private Button button1;
    private Button button2;
    private Button button3;
    private static final String TAG = "googlemap_example";
    private Marker currentMarker = null;
    private List<Marker> previous_marker;
    private LatLng currentPosition;
    private Location mCurrentLocatiion;
    private FragmentActivity mContext;



    private static findfragment INSTANCE = null;
    private  View view;
    private Spinner sp_api;

    private Button search;

    private TextView findlabel;
    private String spinnerSelectedName;

    private ODsayService odsayService;
    private JSONObject jsonObject;

    public static findfragment getINSTANCE(){
        if(INSTANCE == null)
            INSTANCE = new findfragment();
        return INSTANCE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.layout_find, container, false);

        button1 = view.findViewById(R.id.ZIP);
        button2 = view.findViewById(R.id.ZIP2);
        button3 = view.findViewById(R.id.star);

        view = view.findViewById(R.id.layout_find);

        recyclerView = view.findViewById(R.id.recycler_find);
        startSearch = view.findViewById(R.id.start_search);
        endSearch = view.findViewById(R.id.end_search);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        List<Recent> potionList = new ArrayList<Recent>();
        for (int i = 0; i < 30; i++) {
            Recent rc = new Recent(i + "", "");
            potionList.add(rc);
        }

        for (int i = 0; i < 30; i++) {
            Recent rc = new Recent(i + "" + i, "");
            potionList.add(rc);
        }

        for (int i = 0; i < 30; i++) {
            Recent rc = new Recent(i + "" + i + "" + i, "");
            potionList.add(rc);
        }

        adapter = new SearchAdapter(this, potionList);
        recyclerView.setAdapter(adapter);

        startSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = startSearch.getText().toString()
                        .toLowerCase(Locale.getDefault());
                adapter.filter(text);

            }
        });

        endSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = endSearch.getText().toString()
                        .toLowerCase(Locale.getDefault());
                adapter.filter(text);

            }
        });



       // sp_api.setSelection(0);
        //initdata();
        //mapView.getMapAsync(this);
        return view;
    }

    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        //Fragment에서의 onCreateView를 마치고, Activity에서 onCreate()가 호출되고 나서 호출됨
        //Activity와 Fragment의 뷰가 모두 생성된 상태로, view를 변경하는 작업 가능
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");

        //액티비티가 처음 생성될 때 실행되는 함수
        MapsInitializer.initialize(mContext);
        //Initialize places
        Places.initialize(mContext.getApplicationContext(), "AIzaSyBGN9HuTUWakZjy19FTkGPw4KZML3sbJfc");

        //Set EditText non focusable
        startSearch.setFocusable(false);
        startSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);
                //Create intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.
                        OVERLAY, fieldList).build(mContext);
                //Start activity result
                startActivityForResult(intent, 100);
            }
        });

        endSearch.setFocusable(false);
        endSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);
                //Create intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.
                        OVERLAY, fieldList).build(mContext);
                //Start activity result
                startActivityForResult(intent, 100);
            }
        });
        previous_marker = new ArrayList<Marker>();
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


    private void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        currentMarker = fMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        //mMap.moveCamera(cameraUpdate);  //현재위치로 계속 이동

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
    public void onMapReady(final GoogleMap googleMap) {
        fMap = googleMap;

        // 맵 터치 이벤트 구현 //
        fMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("마커 좌표");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                googleMap.addMarker(mOptions);
            }
        });

        ////////////////////

        // 버튼 이벤트
        search.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str1=startSearch.getText().toString();
                String str2=endSearch.getText().toString();
                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str1, // 주소
                            10); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(addressList.get(0).toString());
                // 콤마를 기준으로 split
                String []splitStr = addressList.get(0).toString().split(",");
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                System.out.println(address);

                String latitude1 = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                String longitude1 = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                System.out.println(latitude1);
                System.out.println(longitude1);

                // 좌표(위도, 경도) 생성
                LatLng point = new LatLng(Double.parseDouble(latitude1), Double.parseDouble(longitude1));
                // 마커 생성
                MarkerOptions mOptions1 = new MarkerOptions();
                mOptions1.title("search result");
                mOptions1.snippet(address);
                mOptions1.position(point);
                // 마커 추가
                fMap.addMarker(mOptions1);
                // 해당 좌표로 화면 줌
                fMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));

                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str2, // 주소
                            10); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(addressList.get(0).toString());
                // 콤마를 기준으로 split
                String []splitStr2 = addressList.get(0).toString().split(",");
                String address2 = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                System.out.println(address2);

                String latitude2 = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                String longitude2 = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                System.out.println(latitude2);
                System.out.println(longitude2);

                // 좌표(위도, 경도) 생성
                LatLng point2 = new LatLng(Double.parseDouble(latitude2), Double.parseDouble(longitude2));
                // 마커 생성
                MarkerOptions mOptions2 = new MarkerOptions();
                mOptions2.title("search result");
                mOptions2.snippet(address);
                mOptions2.position(point);
                // 마커 추가
                fMap.addMarker(mOptions2);
                // 해당 좌표로 화면 줌
                fMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));

            }
        });


        ////////////////////

        // Add a marker in Sydney and move the camera
      //  LatLng sydney = new LatLng(-34, 151);
        //fMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //fMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }





   @Override
   public void onAttach(Context context) {

       super.onAttach(context);
        this.mContext= (FragmentActivity) context;
    }//context 가져오려면 필요한 함수. 프래그먼트에서는 컨텍스트를 함부로 못가져와서 붙임

    private void init() {


        mContext = (FragmentActivity) this.getContext();//컨텍스트 변수

        odsayService = ODsayService.init(mContext, getString(R.string.odsay_key));//odsayservice객체생성
        odsayService.setReadTimeout(5000);//서버연결제한시간설정(단위는 밀리세컨드 현재 5초로 설정)
        odsayService.setConnectionTimeout(5000);//데이터획득제한시간설정(위와 동일)

//        bt_api_call.setOnClickListener(onClickListener);
        //sp_api.setOnItemSelectedListener(onItemSelectedListener);
    }



    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            spinnerSelectedName = (String) parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
            jsonObject = oDsayData.getJson();
            findlabel.setText(jsonObject.toString());
            //API 호출 결과 데이터를 리턴합니다. Json, Map 형식의 데이터 서비스를 제공하고,
            //getJson, getMap 메서드를 통해 가져옵니다.
            // - odsayData : API 호출 결과 데이터를 리턴합니다. Json, Map 형식의 데이터 서비스를 제공하고,
            //   getJson, getMap 메서드를 통해 가져옵니다.
            //   - api : 호출한 API 값 종류를 리턴합니다. API명은 호출 메서드 네이밍을 따라갑니다.
        }

        @Override
        public void onError(int code, String errorMessage, API api) {
            findlabel.setText("API : " + api.name() + "\n" + errorMessage);
        }//code<-에러코드값, 호출한 api값종류와 에러 메세지 리턴
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (spinnerSelectedName) {
                case "버스 노선 조회":
                    odsayService.requestSearchBusLane("150", "1000", "no", "10", "1", onResultCallbackListener);
                    break;
                case "버스노선 상세정보 조회":
                    odsayService.requestBusLaneDetail("12018", onResultCallbackListener);
                    break;
                case "버스정류장 세부정보 조회":
                    odsayService.requestBusStationInfo("107475", onResultCallbackListener);
                    break;
                case "열차•KTX 운행정보 검색":
                    odsayService.requestTrainServiceTime("3300128", "3300108", onResultCallbackListener);
                    break;
                case "고속버스 운행정보 검색":
                    odsayService.requestExpressServiceTime("4000057", "4000030", onResultCallbackListener);
                    break;
                case "시외버스 운행정보 검색":
                    odsayService.requestIntercityServiceTime("4000022", "4000255", onResultCallbackListener);
                    break;
                case "항공 운행정보 검색":
                    odsayService.requestAirServiceTime("3500001", "3500003", "6", onResultCallbackListener);
                    break;
                case "운수회사별 버스노선 조회":
                    odsayService.requestSearchByCompany("792", "100", onResultCallbackListener);
                    break;
                case "지하철역 세부 정보 조회":
                    odsayService.requestSubwayStationInfo("130", onResultCallbackListener);
                    break;
                case "지하철역 전체 시간표 조회":
                    odsayService.requestSubwayTimeTable("130", "1", onResultCallbackListener);
                    break;
                case "노선 그래픽 데이터 검색":
                    odsayService.requestLoadLane("0:0@12018:1:-1:-1", onResultCallbackListener);
                    break;
                case "대중교통 정류장 검색":
                    odsayService.requestSearchStation("11", "1000", "1:2", "10", "1", "127.0363583:37.5113295", onResultCallbackListener);
                    break;
                case "반경내 대중교통 POI 검색":
                    odsayService.requestPointSearch("126.933361407195", "37.3643392278118", "250", "1:2", onResultCallbackListener);
                    break;
                case "지도 위 대중교통 POI 검색":
                    odsayService.requestBoundarySearch("127.045478316811:37.68882830829:127.055063420699:37.6370465749586", "127.045478316811:37.68882830829:127.055063420699:37.6370465749586", "1:2", onResultCallbackListener);
                    break;
                case "지하철 경로검색 조회(지하철 노선도)":
                    odsayService.requestSubwayPath("1000", "201", "222", "1", onResultCallbackListener);
                    break;
                case "대중교통 길찾기":
                    odsayService.requestSearchPubTransPath("126.926493082645", "37.6134436427887", "127.126936754911", "37.5004198786564", "0", "0", "0", onResultCallbackListener);
                    break;
                case "지하철역 환승 정보 조회":
                    odsayService.requestSubwayTransitInfo("133", onResultCallbackListener);
                    break;
                case "고속버스 터미널 조회":
                    odsayService.requestExpressBusTerminals("1000", "서울", onResultCallbackListener);
                    break;
                case "시외버스 터미널 조회":
                    odsayService.requestIntercityBusTerminals("1000", "서울", onResultCallbackListener);
                    break;
                case "도시코드 조회":
                    odsayService.requestSearchCID("서울", onResultCallbackListener);
                    break;
            }//odsay서비스객체들 파라미터를 통해 api호출하고 onResultCallbackListener를 통해 결과 반환
        }
    };

    private void initdata() {
        //초기화
        items.clear();
        items.add(new Item("2020년 5월 15일", "민주랑 약속 "));
        items.add(new Item("2020년 5월 17일", "가족모임 "));
        items.add(new Item("2020년 5월 18일", "초등학교 동창회"));
    }


    @Override
    public void onPlacesFailure(PlacesException e) {
        
    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(List<noman.googleplaces.Place> places) {

    }

    @Override
    public void onPlacesFinished() {

    }
}
