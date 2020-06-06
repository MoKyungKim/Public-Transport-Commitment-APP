package com.example.cooperativeproject2.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cooperativeproject2.Adapter.SearchAdapter;
import com.example.cooperativeproject2.Item;
import com.example.cooperativeproject2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//위치 받아오기
//길찾기


//class MapsActivity : AppCompatActivity(), OnMapReadyCallback {



  //      override fun onCreate(savedInstanceState: Bundle?) {
    //    super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_maps)
        //// Obtain the SupportMapFragment and get notified when the map is ready to be used.
       // val mapFragment = supportFragmentManager
       // .findFragmentById(R.id.map) as SupportMapFragment
       // mapFragment.getMapAsync(this)
        //}


public class findfragment extends Fragment implements OnMapReadyCallback {


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
    private Geocoder geocoder;
    private Button button;





    private static findfragment INSTANCE = null;
    private  View view;
    private Context context;
    private Spinner sp_api;
    private RadioGroup rg_object_type;
    private RadioButton rb_json, rb_map;

    private Button bt_api_call;
    private TextView tv_data;

    private String spinnerSelectedName;

    private ODsayService odsayService;
    private JSONObject jsonObject;
    private Map mapObject;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view= inflater.inflate(R.layout.layout_find, container, false);



        mapView = view.findViewById(R.id.map);
        button = view.findViewById(R.id.ZIP);
        button = view.findViewById(R.id.ZIP2);
        button = view.findViewById(R.id.star);

        view = view.findViewById(R.id.layout_find);


        recyclerView = view.findViewById(R.id.recycler_find);
        startSearch = view.findViewById(R.id.start_search);
        endSearch =view.findViewById(R.id.end_search);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        List<Recent> potionList = new ArrayList<Recent>();
        for(int i=0;i<30;i++){
            Recent rc = new Recent(i+"","");
            potionList.add(rc);
        }

        for(int i=0;i<30;i++){
            Recent rc = new Recent(i+""+i,"");
            potionList.add(rc);
        }

        for(int i=0;i<30;i++){
            Recent rc = new Recent(i+""+i+""+i,"");
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
        initdata();
        return view;
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
        button.setOnClickListener(new Button.OnClickListener(){
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
                String []splitStr = addressList.get(0).toString().split(",");
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                System.out.println(address);

                String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                System.out.println(latitude);
                System.out.println(longitude);

                // 좌표(위도, 경도) 생성
                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
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
        this.context= context;
    }//context 가져오려면 필요한 함수. 프래그먼트에서는 컨텍스트를 함부로 못가져와서 붙임

    private void init() {


        context = this.getContext();//컨텍스트 변수

        odsayService = ODsayService.init(context, getString(R.string.odsay_key));//odsayservice객체생성
        odsayService.setReadTimeout(5000);//서버연결제한시간설정(단위는 밀리세컨드 현재 5초로 설정)
        odsayService.setConnectionTimeout(5000);//데이터획득제한시간설정(위와 동일)

//        bt_api_call.setOnClickListener(onClickListener);
        //sp_api.setOnItemSelectedListener(onItemSelectedListener);
        rg_object_type.setOnCheckedChangeListener(onCheckedChangeListener);
    }


    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            if (rg_object_type.getCheckedRadioButtonId() == rb_json.getId()) {
                tv_data.setText(jsonObject.toString());
            } else if (rg_object_type.getCheckedRadioButtonId() == rb_map.getId()) {
                tv_data.setText(mapObject.toString());
            }
        }
    };
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
            mapObject = oDsayData.getMap();
            if (rg_object_type.getCheckedRadioButtonId() == rb_json.getId()) {
                tv_data.setText(jsonObject.toString());
            } else if (rg_object_type.getCheckedRadioButtonId() == rb_map.getId()) {
                tv_data.setText(mapObject.toString());
            }//API 호출 결과 데이터를 리턴합니다. Json, Map 형식의 데이터 서비스를 제공하고,
            //getJson, getMap 메서드를 통해 가져옵니다.
            // - odsayData : API 호출 결과 데이터를 리턴합니다. Json, Map 형식의 데이터 서비스를 제공하고,
            //   getJson, getMap 메서드를 통해 가져옵니다.
            //   - api : 호출한 API 값 종류를 리턴합니다. API명은 호출 메서드 네이밍을 따라갑니다.
        }

        @Override
        public void onError(int code, String errorMessage, API api) {
            tv_data.setText("API : " + api.name() + "\n" + errorMessage);
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

}
