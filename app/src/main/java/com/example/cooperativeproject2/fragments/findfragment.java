package com.example.cooperativeproject2.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cooperativeproject2.Adapter.SearchAdapter;
import com.example.cooperativeproject2.R;
import com.example.cooperativeproject2.itemactivity;
import com.example.cooperativeproject2.object;
import com.example.cooperativeproject2.routeActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

//import com.google.android.libraries.places.api.model.Place;


//위치 받아오기
//길찾기



public class findfragment extends Fragment implements
        ActivityCompat.OnRequestPermissionsResultCallback, PlacesListener {



    private ArrayList<String[]> clickTraffic= new ArrayList<>();
    private ArrayList<object> items = new ArrayList<>();
    private ArrayList<String[]> SX = new ArrayList<>();
    private ArrayList<String[]> SY = new ArrayList<>();
    private ArrayList<String[]> EX = new ArrayList<>();
    private ArrayList<String[]> EY = new ArrayList<>();
    private ArrayList<String[]> StartName= new ArrayList<>();
    private ArrayList<String[]> EndName= new ArrayList<>();
    // 데이터를 넣은 리스트변수
    private RecyclerView recyclerView;          // 검색을 보여줄 리스트변수
    private EditText startSearch;        // 출발 검색어를 입력할 Input 창
    private EditText endSearch;        // 도착 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private LinearLayoutManager mLayoutManager;

    private int StartOrEnd = 0;
    private int startendcheck = 0;
    String name=((mapfragment) mapfragment.context).markerName;
    Double lat=((mapfragment) mapfragment.context).late;
    Double lng=((mapfragment) mapfragment.context).lnge;
    int check=((mapfragment) mapfragment.context).startendcheck;

    Double StartLat;
    Double StartLng;
    Double EndLat;
    Double EndLng;

    String sat, sng, eat, eng;


    //구글맵이용

    private static final String TAG = "googlemap_example";
    private FragmentActivity mContext;



    private static findfragment INSTANCE = null;
    private  View view;
    private Spinner sp_api;

    private Button search;

    private ODsayService odsayService;
    private JSONObject jsonobject;
    private JSONObject jresult;

    Intent myIntent;



    public static findfragment getINSTANCE(){
        if(INSTANCE == null)
            INSTANCE = new findfragment();
        return INSTANCE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }
    private void init() {


        mContext = (FragmentActivity) this.getContext();//컨텍스트 변수

        odsayService = ODsayService.init(mContext, getString(R.string.odsay_key));//odsayservice객체생성
        odsayService.setReadTimeout(5000);//서버연결제한시간설정(단위는 밀리세컨드 현재 5초로 설정)
        odsayService.setConnectionTimeout(5000);//데이터획득제한시간설정(위와 동일)

//        bt_api_call.setOnClickListener(onClickListener);
        //sp_api.setOnItemSelectedListener(onItemSelectedListener);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("체크","oncreateview");

        view = inflater.inflate(R.layout.layout_find, container, false);


        view = view.findViewById(R.id.layout_find);

        recyclerView = view.findViewById(R.id.recycler_find);
        startSearch = view.findViewById(R.id.start_search);
        endSearch = view.findViewById(R.id.end_search);
        search = view.findViewById(R.id.search);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);


        initdata();
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_find);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        SearchAdapter adapter = new SearchAdapter( items);
        Log.d("체크","recitems ="+items);

        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener()  {
            @Override
            public void onClick(@Nonnull View view, int position)
            {

                myIntent = new Intent(getActivity(), itemactivity.class);
                myIntent.putExtra("traffic",clickTraffic.get(position));
                startActivity(myIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

                Intent mapIntent = new Intent(getActivity(), routeActivity.class);
                mapIntent.putExtra("SX", SX.get(position));

                mapIntent.putExtra("SY", SY.get(position));
                mapIntent.putExtra("EX", EX.get(position));
                mapIntent.putExtra("EY", EY.get(position));
                mapIntent.putExtra("SLat",StartLat); mapIntent.putExtra("SLng",StartLng);
                mapIntent.putExtra("ELat",EndLat); mapIntent.putExtra("ELng",EndLng);
                mapIntent.putExtra("startname", StartName.get(position));
                mapIntent.putExtra("endname", EndName.get(position));
                mapIntent.putExtra("SLat",sat);mapIntent.putExtra("SLng",sng);
                mapIntent.putExtra("ELat",eat);mapIntent.putExtra("ELng",eng);
                Log.d("???", sat+" ");Log.d("???", sng+" ");
                Log.d("???", eat+" "); Log.d("???", eat+" ");
                startActivity(mapIntent);

            }
        }));





        return view;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private findfragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final findfragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }




    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        //Fragment에서의 onCreateView를 마치고, Activity에서 onCreate()가 호출되고 나서 호출됨
        //Activity와 Fragment의 뷰가 모두 생성된 상태로, view를 변경하는 작업 가능
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        Log.d("체크","onactivityCreated");

        //액티비티가 처음 생성될 때 실행되는 함수
        MapsInitializer.initialize(mContext);
        //Initialize places
        Places.initialize(mContext.getApplicationContext(), "AIzaSyBGN9HuTUWakZjy19FTkGPw4KZML3sbJfc");

        search.setFocusable(false);
        search.setOnClickListener (new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                {
                    initdata();
                    name = ((mapfragment) mapfragment.context).markerName;
                    lat = ((mapfragment) mapfragment.context).late;
                    lng = ((mapfragment) mapfragment.context).lnge;
                    if (!startSearch.getText().toString().equals("") && !endSearch.getText().toString().equals("")) {
                        odsayService.requestSearchPubTransPath(String.valueOf(StartLng), String.valueOf(StartLat), String.valueOf(EndLng), String.valueOf(EndLat), "0", "0", "0", onResultCallbackListener);
                    } else {
                        check = ((mapfragment) mapfragment.context).startendcheck;
                        if (check == 1) {
                            startSearch.setText(name);
                            StartLat = lat;
                            StartLng = lng;
                            Log.d("체크", "출발" + name + " " + StartLat + " " + StartLng);
                            check = 0;
                        } else if (check == 2) {
                            endSearch.setText(name);
                            EndLng = lng;
                            EndLat = lat;
                            Log.d("체크", "도착" + name + " " + EndLat + " " + EndLng);
                            check = 0;
                        }
                    }
                }
            }

        });
        //Set EditText non focusable
        startSearch.setFocusable(false);
        startSearch.setOnClickListener(v -> {
            //Initialize place field list
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                    Place.Field.LAT_LNG, Place.Field.NAME);
            //Create intent
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.
                    OVERLAY, fieldList).build(getActivity());
            //Start activity result
            startActivityForResult(intent, 102);
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
                startActivityForResult(intent, 101);
            }
        });








    }



    public OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
            try {
                initdata();
                jsonobject = oDsayData.getJson();
                jresult = (JSONObject) jsonobject.get("result");
                JSONArray jpath = (JSONArray) jresult.getJSONArray("path");
                int pl = jpath.length();

                for (int i = 0; i < pl; i++) {
                    JSONObject path = (JSONObject) jpath.getJSONObject(i);
                    int pathtype = path.getInt("pathType");

                    if (pathtype == 1) {
                        JSONObject info = (JSONObject) path.getJSONObject("info");
                        Integer payment = info.getInt("payment");
                        Integer totaltime = info.getInt("totalTime");
                        String first = info.getString("firstStartStation");
                        String last = info.getString("lastEndStation");
                        Integer totalstation = info.getInt("totalStationCount");
                        Double totaldistance = info.getDouble("totalDistance");
                        JSONArray subpath = (JSONArray) path.getJSONArray("subPath");
                        Double[] distance = new Double[subpath.length()];
                        Integer[] sectionTime = new Integer[subpath.length()];
                        String[] Startname = new String[subpath.length()];
                        String[] Endname = new String[subpath.length()];
                        String[] way = new String[subpath.length()];
                        String[] traffic = new String[subpath.length()];
                        String[] sX = new String[subpath.length()];
                        String[] sY = new String[subpath.length()];
                        String[] eX = new String[subpath.length()];
                        String[] eY = new String[subpath.length()];


                        for (int j = 0; j < subpath.length(); j++) {
                            JSONObject sub = (JSONObject) subpath.getJSONObject(j);
                            int traffictype = sub.getInt("trafficType");
                            if (traffictype == 1) {
                                distance[j] = sub.getDouble("distance");
                                sectionTime[j] = sub.getInt("sectionTime");
                                String startname = sub.getString("startName");
                                way[j] = sub.getString("way");
                                Double startX = sub.getDouble("startX");
                                Double startY = sub.getDouble("startY");
                                String endname = sub.getString("endName");
                                Double endX = sub.getDouble("endX");
                                Double endY = sub.getDouble("endY");


                                JSONArray jlane = (JSONArray) sub.getJSONArray("lane");
                                String[] subway = new String[jlane.length()];
                                for (int k = 0; k < jlane.length(); k++) {
                                    JSONObject lane = (JSONObject) jlane.getJSONObject(k);
                                    String name = lane.getString("name");
                                    //Integer subwaycode = lane.getInt("subwayCode");
                                    subway[k] = name;

                                }

                                JSONObject passstoplist = (JSONObject) sub.getJSONObject("passStopList");
                                JSONArray stations = (JSONArray) passstoplist.getJSONArray("stations");
                                int psl = stations.length();
                                String[] pass = new String[psl];

                                for (int k = 0; k < psl; k++) {
                                    JSONObject pst = (JSONObject) stations.getJSONObject(k);
                                    Integer index = pst.getInt("index");
                                    String stationname = pst.getString("stationName");
                                    pass[k] = ((index + 1) + "." + stationname + " ");

                                }

                                traffic[j] = "지하철  " + " \n시간 : " + sectionTime[j] + "분" + " \n노선 : " + Arrays.toString(subway) + " \n정류장 : " + Arrays.toString(pass);

                                sX[j] = String.valueOf(startX);
                                sY[j] = String.valueOf(startY);
                                eX[j] = String.valueOf(endX);
                                eY[j] = String.valueOf(endY);
                                Startname[j] = startname;
                                Endname[j] = endname;

                            } else if (traffictype == 2) {

                                distance[j] = sub.getDouble("distance");
                                sectionTime[j] = sub.getInt("sectionTime");
                                String startname = sub.getString("startName");
                                Double startX = sub.getDouble("startX");
                                Double startY = sub.getDouble("startY");
                                String endname = sub.getString("endName");
                                Double endX = sub.getDouble("endX");
                                Double endY = sub.getDouble("endY");


                                JSONArray jlane = (JSONArray) sub.getJSONArray("lane");
                                String[] bus = new String[jlane.length()];
                                for (int k = 0; k < jlane.length(); k++) {
                                    JSONObject lane = (JSONObject) jlane.getJSONObject(k);
                                    String busno = lane.getString("busNo");
                                    bus[k] = busno;

                                }

                                JSONObject passstoplist = (JSONObject) sub.getJSONObject("passStopList");
                                JSONArray stations = (JSONArray) passstoplist.getJSONArray("stations");
                                int psl = stations.length();
                                String[] pass = new String[psl];

                                for (int k = 0; k < psl; k++) {
                                    JSONObject pst = (JSONObject) stations.getJSONObject(k);
                                    Integer index = pst.getInt("index");
                                    String stationname = pst.getString("stationName");
                                    pass[k] = (index + 1) + "." + stationname + " ";

                                }

                                traffic[j] = "버스  " + " \n소요시간 : " + sectionTime[j] + "분\n" + "버스번호: " + Arrays.toString(bus) + " \n정류장 : " + Arrays.toString(pass);
                                sX[j] = String.valueOf(startX);
                                sY[j] = String.valueOf(startY);
                                eX[j] = String.valueOf(endX);
                                eY[j] = String.valueOf(endY);
                                Startname[j] = startname;
                                Endname[j] = endname;


                            } else if (traffictype == 3) {

                                distance[j] = sub.getDouble("distance");
                                sectionTime[j] = sub.getInt("sectionTime");
                                traffic[j] = "도보 " + " \n거리 : " + distance[j] + "미터" + " \n소요시간 : " + sectionTime[j] + "분";

                            }


                        }

                        clickTraffic.add(i, traffic);


                        Log.d("check", first + ". " + last + ". " + totalstation + ". " + totaldistance + "+");
                        items.add(new object("총요금 : " + payment + "원" + "\n총 소요시간 : " + (totaltime / 60) + "시간" + (totaltime % 60) + "분" +
                                "\n출발역 : " + first + "\n도착역 : " + last + "\n 총 정류장 수 : " + totalstation + "개"));
                        Toast.makeText(getActivity(), sX+" ", Toast.LENGTH_SHORT).show();
                        SX.add(i, sX);
                        SY.add(i, sY);
                        EX.add(i, eX);
                        EY.add(i, eY);
                        StartName.add(i, Startname);
                        EndName.add(i, Endname);


                    } else if (pathtype == 2) {
                        JSONObject info = (JSONObject) path.getJSONObject("info");
                        Integer payment = info.getInt("payment");
                        Integer totaltime = info.getInt("totalTime");
                        String first = info.getString("firstStartStation");
                        String last = info.getString("lastEndStation");
                        Integer totalstation = info.getInt("totalStationCount");
                        Double totaldistance = info.getDouble("totalDistance");
                        JSONArray subpath = (JSONArray) path.getJSONArray("subPath");
                        double[] distance = new double[subpath.length()];
                        Integer[] sectionTime = new Integer[subpath.length()];
                        String[] Startname = new String[subpath.length()];
                        String[] Endname = new String[subpath.length()];
                        String[] way = new String[subpath.length()];
                        String[] traffic = new String[subpath.length()];
                        String[] sX = new String[subpath.length()];
                        String[] sY = new String[subpath.length()];
                        String[] eX = new String[subpath.length()];
                        String[] eY = new String[subpath.length()];


                        for (int j = 0; j < subpath.length(); j++) {
                            JSONObject sub = (JSONObject) subpath.getJSONObject(j);
                            int traffictype = sub.getInt("trafficType");
                            if (traffictype == 1) {
                                distance[j] = sub.getDouble("distance");
                                sectionTime[j] = sub.getInt("sectionTime");
                                String startname = sub.getString("startName");
                                way[j] = sub.getString("way");
                                Double startX = sub.getDouble("startX");
                                Double startY = sub.getDouble("startY");
                                String endname = sub.getString("endName");
                                Double endX = sub.getDouble("endX");
                                Double endY = sub.getDouble("endY");


                                JSONArray jlane = (JSONArray) sub.getJSONArray("lane");
                                String[] subway = new String[jlane.length()];
                                for (int k = 0; k < jlane.length(); k++) {
                                    JSONObject lane = (JSONObject) jlane.getJSONObject(k);
                                    String name = lane.getString("name");
                                    //Integer subwaycode = lane.getInt("subwayCode");
                                    subway[k] = name;

                                }

                                JSONObject passstoplist = (JSONObject) sub.getJSONObject("passStopList");
                                JSONArray stations = (JSONArray) passstoplist.getJSONArray("stations");
                                int psl = stations.length();
                                String[] pass = new String[psl];

                                for (int k = 0; k < psl; k++) {
                                    JSONObject pst = (JSONObject) stations.getJSONObject(k);
                                    Integer index = pst.getInt("index");
                                    String stationname = pst.getString("stationName");
                                    pass[k] = ((index + 1) + "." + stationname + " ");

                                }

                                traffic[j] = "지하철  " + " \n시간 : " + sectionTime[j] + "분" + " \n노선 : " + Arrays.toString(subway) + " \n정류장 : " + Arrays.toString(pass);

                                sX[j] = String.valueOf(startX);
                                sY[j] = String.valueOf(startY);
                                eX[j] = String.valueOf(endX);
                                eY[j] = String.valueOf(endY);
                                Startname[j] = startname;
                                Endname[j] = endname;

                            } else if (traffictype == 2) {

                                distance[j] = sub.getDouble("distance");
                                sectionTime[j] = sub.getInt("sectionTime");
                                String startname = sub.getString("startName");
                                Double startX = sub.getDouble("startX");
                                Double startY = sub.getDouble("startY");
                                String endname = sub.getString("endName");
                                Double endX = sub.getDouble("endX");
                                Double endY = sub.getDouble("endY");


                                JSONArray jlane = (JSONArray) sub.getJSONArray("lane");
                                String[] bus = new String[jlane.length()];
                                for (int k = 0; k < jlane.length(); k++) {
                                    JSONObject lane = (JSONObject) jlane.getJSONObject(k);
                                    String busno = lane.getString("busNo");
                                    bus[k] = busno;

                                }

                                JSONObject passstoplist = (JSONObject) sub.getJSONObject("passStopList");
                                JSONArray stations = (JSONArray) passstoplist.getJSONArray("stations");
                                int psl = stations.length();
                                String[] pass = new String[psl];

                                for (int k = 0; k < psl; k++) {
                                    JSONObject pst = (JSONObject) stations.getJSONObject(k);
                                    Integer index = pst.getInt("index");
                                    String stationname = pst.getString("stationName");
                                    pass[k] = (index + 1) + "." + stationname + " ";

                                }

                                traffic[j] = "버스  " + " \n소요시간 : " + sectionTime[j] + "분\n" + "버스번호: " + Arrays.toString(bus) + " \n정류장 : " + Arrays.toString(pass);
                                sX[j] = String.valueOf(startX);
                                sY[j] = String.valueOf(startY);
                                eX[j] = String.valueOf(endX);
                                eY[j] = String.valueOf(endY);
                                Startname[j] = startname;
                                Endname[j] = endname;


                            } else if (traffictype == 3) {

                                distance[j] = sub.getDouble("distance");
                                sectionTime[j] = sub.getInt("sectionTime");
                                traffic[j] = "도보 " + " \n거리 : " + distance[j] + "미터" + " \n소요시간 : " + sectionTime[j] + "분";

                            }


                        }

                        clickTraffic.add(i, traffic);


                        Log.d("check", first + ". " + last + ". " + totalstation + ". " + totaldistance + "+");
                        items.add(new object("총요금 : " + payment + "원" + "\n총 소요시간 : " + (totaltime / 60) + "시간" + (totaltime % 60) + "분" +
                                "\n출발역 : " + first + "\n도착역 : " + last + "\n 총 정류장 수 : " + totalstation + "개"));
                        Toast.makeText(getActivity(), sX+" ", Toast.LENGTH_SHORT).show();
                        SX.add(i, sX);
                        SY.add(i, sY);
                        EX.add(i, eX);
                        EY.add(i, eY);
                        StartName.add(i, Startname);
                        EndName.add(i, Endname);
                    } else if (pathtype == 3) {
                        JSONObject info = (JSONObject) path.getJSONObject("info");
                        Integer payment = info.getInt("payment");
                        Integer totaltime = info.getInt("totalTime");
                        String first = info.getString("firstStartStation");
                        String last = info.getString("lastEndStation");
                        Integer totalstation = info.getInt("totalStationCount");
                        Double totaldistance = info.getDouble("totalDistance");
                        JSONArray subpath = (JSONArray) path.getJSONArray("subPath");
                        Double[] distance = new Double[subpath.length()];
                        Integer[] sectionTime = new Integer[subpath.length()];
                        String[] Startname = new String[subpath.length()];
                        String[] Endname = new String[subpath.length()];
                        String[] way = new String[subpath.length()];
                        String[] traffic = new String[subpath.length()];
                        String[] sX = new String[subpath.length()];
                        String[] sY = new String[subpath.length()];
                        String[] eX = new String[subpath.length()];
                        String[] eY = new String[subpath.length()];


                        for (int j = 0; j < subpath.length(); j++) {
                            JSONObject sub = (JSONObject) subpath.getJSONObject(j);
                            int traffictype = sub.getInt("trafficType");
                            if (traffictype == 1) {
                                distance[j] = sub.getDouble("distance");
                                sectionTime[j] = sub.getInt("sectionTime");
                                String startname = sub.getString("startName");
                                way[j] = sub.getString("way");
                                Double startX = sub.getDouble("startX");
                                Double startY = sub.getDouble("startY");
                                String endname = sub.getString("endName");
                                Double endX = sub.getDouble("endX");
                                Double endY = sub.getDouble("endY");


                                JSONArray jlane = (JSONArray) sub.getJSONArray("lane");
                                String[] subway = new String[jlane.length()];
                                for (int k = 0; k < jlane.length(); k++) {
                                    JSONObject lane = (JSONObject) jlane.getJSONObject(k);
                                    String name = lane.getString("name");
                                    //Integer subwaycode = lane.getInt("subwayCode");
                                    subway[k] = name;

                                }

                                JSONObject passstoplist = (JSONObject) sub.getJSONObject("passStopList");
                                JSONArray stations = (JSONArray) passstoplist.getJSONArray("stations");
                                int psl = stations.length();
                                String[] pass = new String[psl];

                                for (int k = 0; k < psl; k++) {
                                    JSONObject pst = (JSONObject) stations.getJSONObject(k);
                                    Integer index = pst.getInt("index");
                                    String stationname = pst.getString("stationName");
                                    pass[k] = ((index + 1) + "." + stationname + " ");

                                }

                                traffic[j] = "지하철  " + " \n시간 : " + sectionTime[j] + "분" + " \n노선 : " + Arrays.toString(subway) + " \n정류장 : " + Arrays.toString(pass);

                                sX[j] = String.valueOf(startX);
                                sY[j] = String.valueOf(startY);
                                eX[j] = String.valueOf(endX);
                                eY[j] = String.valueOf(endY);
                                Startname[j] = startname;
                                Endname[j] = endname;

                            } else if (traffictype == 2) {

                                distance[j] = sub.getDouble("distance");
                                sectionTime[j] = sub.getInt("sectionTime");
                                String startname = sub.getString("startName");
                                Double startX = sub.getDouble("startX");
                                Double startY = sub.getDouble("startY");
                                String endname = sub.getString("endName");
                                Double endX = sub.getDouble("endX");
                                Double endY = sub.getDouble("endY");


                                JSONArray jlane = (JSONArray) sub.getJSONArray("lane");
                                String[] bus = new String[jlane.length()];
                                for (int k = 0; k < jlane.length(); k++) {
                                    JSONObject lane = (JSONObject) jlane.getJSONObject(k);
                                    String busno = lane.getString("busNo");
                                    bus[k] = busno;

                                }

                                JSONObject passstoplist = (JSONObject) sub.getJSONObject("passStopList");
                                JSONArray stations = (JSONArray) passstoplist.getJSONArray("stations");
                                int psl = stations.length();
                                String[] pass = new String[psl];

                                for (int k = 0; k < psl; k++) {
                                    JSONObject pst = (JSONObject) stations.getJSONObject(k);
                                    Integer index = pst.getInt("index");
                                    String stationname = pst.getString("stationName");
                                    pass[k] = (index + 1) + "." + stationname + " ";

                                }

                                traffic[j] = "버스  " + " \n소요시간 : " + sectionTime[j] + "분\n" + "버스번호: " + Arrays.toString(bus) + " \n정류장 : " + Arrays.toString(pass);
                                sX[j] = String.valueOf(startX);
                                sY[j] = String.valueOf(startY);
                                eX[j] = String.valueOf(endX);
                                eY[j] = String.valueOf(endY);
                                Startname[j] = startname;
                                Endname[j] = endname;


                            } else if (traffictype == 3) {

                                distance[j] = sub.getDouble("distance");
                                sectionTime[j] = sub.getInt("sectionTime");
                                traffic[j] = "도보 " + " \n거리 : " + distance[j] + "미터" + " \n소요시간 : " + sectionTime[j] + "분";

                            }


                        }

                        clickTraffic.add(i, traffic);


                        Log.d("check", first + ". " + last + ". " + totalstation + ". " + totaldistance + "+");
                        items.add(new object("총요금 : " + payment + "원" + "\n총 소요시간 : " + (totaltime / 60) + "시간" + (totaltime % 60) + "분" +
                                "\n출발역 : " + first + "\n도착역 : " + last + "\n 총 정류장 수 : " + totalstation + "개"));
                        Toast.makeText(getActivity(), sX+" ", Toast.LENGTH_SHORT).show();
                        SX.add(i, sX);
                        SY.add(i, sY);
                        EX.add(i, eX);
                        EY.add(i, eY);
                        StartName.add(i, Startname);
                        EndName.add(i, Endname);
                    }
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }





        }
        //API 호출 결과 데이터를 리턴합니다. Json, Map 형식의 데이터 서비스를 제공하고,
        //getJson, getMap 메서드를 통해 가져옵니다.
        // - odsayData : API 호출 결과 데이터를 리턴합니다. Json, Map 형식의 데이터 서비스를 제공하고,
        //   getJson, getMap 메서드를 통해 가져옵니다.
        //   - api : 호출한 API 값 종류를 리턴합니다. API명은 호출 메서드 네이밍을 따라갑니다.


        @Override
        public void onError(int code, String errorMessage, API api) {
        }

    };

    private void initdata() {
        //초기화
        items.clear();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 102 && resultCode == AutocompleteActivity.RESULT_OK){
            //When success
            //Initialize plac
            Place place = Autocomplete.getPlaceFromIntent(data);
            //Set address on EditText
            startSearch.setText(place.getAddress());

            String str=startSearch.getText().toString();       //검색창에 아무것도 입력하지 않으면 edittext값이 없어서 튕기는거임

            LatLng latLng = place.getLatLng();
            StartLat = latLng.latitude;
            StartLng = latLng.longitude;
            Log.d("체크",str+" "+StartLat+" "+StartLng);
        } else if(requestCode == 101 && resultCode == AutocompleteActivity.RESULT_OK){
            //When success
            //Initialize plac
            Place place = Autocomplete.getPlaceFromIntent(data);
            //Set address on EditText
            endSearch.setText(place.getAddress());

            String str=endSearch.getText().toString();       //검색창에 아무것도 입력하지 않으면 edittext값이 없어서 튕기는거임

            LatLng latLng = place.getLatLng();
            EndLat = latLng.latitude;
            EndLng = latLng.longitude;
            Log.d("체크",str+" lat="+EndLat+" lng="+ EndLng);

            sat = String.valueOf(StartLat); sng= String.valueOf(StartLng);
            eat = String.valueOf(EndLat);
            eng = String.valueOf(EndLng);
        } else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            //when error
            //initialize status
            Status status = Autocomplete.getStatusFromIntent(data);

        }        // 좌표(위도, 경도) 생성

    }
    @Override
    public void onPlacesFailure(PlacesException e) {
        Log.d("체크","onplacefail");
    }


    @Override
    public void onPlacesStart() {
        Log.d("체크","onplacestart");
    }

    @Override
    public void onPlacesSuccess(List<noman.googleplaces.Place> places) {
        Log.d("체크","onplacessuccess");
    }

    @Override
    public void onPlacesFinished() {
        Log.d("체크","onplacefinished");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("체크", "onpause");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("체크","onstop");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d("체크","ondestroy");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("체크","start");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("체크","resume");
    }

}
