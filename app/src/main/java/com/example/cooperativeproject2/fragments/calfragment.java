package com.example.cooperativeproject2.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooperativeproject2.Adapter.CalendarAdapter;
import com.example.cooperativeproject2.Adapter.RecyclerViewAdapter;
import com.example.cooperativeproject2.AddTaskActivity;
import com.example.cooperativeproject2.DayInfo;
import com.example.cooperativeproject2.Item;
import com.example.cooperativeproject2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;


public class calfragment extends Fragment implements OnClickListener, AdapterView.OnItemClickListener {

    View view;
    /**
     * 연/월 텍스트뷰
     */
    private TextView tvDate;
    private TextView AddDate;       //20.06.18 일정날짜
    /**
     * 그리드뷰 어댑터
     */
    //private GridAdapter gridAdapter;
    /**
     * 일 저장 할 리스트
     */
    private ArrayList<String> dayList;
    /**
     * 그리드뷰
     */
    private GridView gridView;


    //캘린더 변수
    private Calendar mCal;


    public calfragment() {
    }


    public static int SUNDAY = 1;
    public static int MONDAY = 2;
    public static int TUESDAY = 3;
    public static int WEDNSESDAY = 4;
    public static int THURSDAY = 5;
    public static int FRIDAY = 6;
    public static int SATURDAY = 7;

    private TextView mTvCalendarTitle;
    private GridView mGvCalendar;
    private TextView text_date;

    private ArrayList<DayInfo> mDayList;
    private CalendarAdapter mCalendarAdapter;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;

    //6.
    private ArrayList<Item> items = new ArrayList<>();

    int today; int month; int dayOfMonth;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.gv_calendar_activity, container, false);

        ImageView bLastMonth = (ImageView) view.findViewById(R.id.calendar_prev_button);
        ImageView bNextMonth = (ImageView) view.findViewById(R.id.calendar_next_button);
        FloatingActionButton bt_add = (FloatingActionButton) view.findViewById(R.id.Floatingbt_add);

        TextView AddDate = (TextView) view.findViewById(R.id.add_date);     //0618추가
        AddDate.setVisibility(View.INVISIBLE);


        mTvCalendarTitle = (TextView) view.findViewById(R.id.gv_calendar_activity_tv_title);
        mGvCalendar = (GridView) view.findViewById(R.id.gv_calendar_activity_gv_calendar);


        bLastMonth.setOnClickListener(this);
        bNextMonth.setOnClickListener(this);
        //mGvCalendar.setOnItemClickListener();

        mDayList = new ArrayList<DayInfo>();


        //6.
        initDataset();

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(context, items);
        recyclerView.setAdapter(adapter);

        Intent intent = new Intent(getActivity(), AddTaskActivity.class);

        bt_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent.putExtra("year",mThisMonthCalendar.get(Calendar.YEAR));
                intent.putExtra("month",mThisMonthCalendar.get(Calendar.MONTH) + 1);
                intent.putExtra("day",today);
                startActivity(intent);
            }
        });

        mGvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int year = mThisMonthCalendar.get(Calendar.YEAR);
                month = mThisMonthCalendar.get(Calendar.MONTH)+1;
                today = position-dayOfMonth+2;
                text_date.setText(month+" 월 "+today+" 일 ");

                items.clear();

                int date;
                if(today<10){
                    date = (year*1000+month*10)*10+today;
                }
                else{
                    date = year*1000+month*10+today;
                }

                /*
                Amplify.API.query(
                        ModelQuery.list(Todo.class, Todo.DATE.eq(date)),
                        response -> {
                            for (Todo todo : response.getData()) {
                                Log.i("MyAmplifyApp", todo.getTaskName());
                                items.add(new Item(todo.getTaskName(),todo.getDate().toString()));
                            }
                        },
                        error -> Log.e("MyAmplifyApp", "Query failure", error)
                );*/
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }



    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    public void onResume ()
    {
        super.onResume();

        // 이번달 의 캘린더 인스턴스를 생성한다.
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);
    }

    /**
     * 달력을 셋팅한다.
     *
     * @param calendar 달력에 보여지는 이번달의 Calendar 객체
     */
    private void getCalendar (Calendar calendar)
    {
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        mDayList.clear();

        // 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);
        Log.e("지난달 마지막일", calendar.get(Calendar.DAY_OF_MONTH) + "");

        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);
        Log.e("이번달 시작일", calendar.get(Calendar.DAY_OF_MONTH) + "");

        if (dayOfMonth == SUNDAY) {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth - 1) - 1;


        // 캘린더 타이틀(년월 표시)을 세팅한다.
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");

        DayInfo day;

        Log.e("DayOfMOnth", dayOfMonth + "");

        for (int i = 0; i < dayOfMonth - 1; i++) {
            int date = lastMonthStartDay + i;
            day = new DayInfo();
            day.setDay(Integer.toString(date));
            day.setInMonth(false);

            mDayList.add(day);
        }
        for (int i = 1; i <= thisMonthLastDay; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(true);

            mDayList.add(day);
        }
        for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(false);
            mDayList.add(day);
        }

        initCalendarAdapter();
    }

    /**
     * 지난달의 Calendar 객체를 반환합니다.
     *
     * @param calendar
     * @return LastMonthCalendar
     */
    private Calendar getLastMonth (Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }

    /**
     * 다음달의 Calendar 객체를 반환합니다.
     *
     * @param calendar
     * @return NextMonthCalendar
     */
    private Calendar getNextMonth (Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }

    @Override
    public void onItemClick (AdapterView < ? > parent, View v,int position, long arg3)
    {
        Toast.makeText(getActivity(), "hi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick (View v)
    {
        switch (v.getId()) {
            case R.id.calendar_prev_button:
                mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
            case R.id.calendar_next_button:
                mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
        }
    }

    private void initCalendarAdapter ()
    {
        mCalendarAdapter = new CalendarAdapter(this, R.layout.day, mDayList);
        mGvCalendar.setAdapter(mCalendarAdapter);
    }

    //6.
    private void initDataset() {
        //초기화
        items.clear();
        items.add(new Item("(어디서)", "(뭘하는지) "));
        items.add(new Item("2020년 5월 17일", "가족모임 "));
        items.add(new Item("2020년 5월 18일", "초등학교 동창회"));
    }
}