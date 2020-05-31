package com.example.cooperativeproject2.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cooperativeproject2.DayInfo;
import com.example.cooperativeproject2.R;
import com.example.cooperativeproject2.fragments.calfragment;

import java.util.ArrayList;

//import com.example.cooperativeproject2.CalendarAdapter;

//import com.example.cooperativeproject2.CalendarAdapter;

public class CalendarAdapter extends BaseAdapter {

    /**
     * BaseAdapter를 상속받아 구현한 CalendarAdapter
     *
     * @author croute
     * @since 2011.03.08
     */

    private ArrayList<DayInfo> mDayList;
    private Context mContext;
    private int mResource;
    private LayoutInflater mLiInflater;

    /**
     * Adpater 생성자
     *  @param context
     *            컨텍스트
     * @param textResource
     *            레이아웃 리소스
     * @param dayList
     */
    public CalendarAdapter(calfragment context, int textResource, ArrayList<DayInfo> dayList)
    {
        this.mContext = context.getActivity();
        this.mDayList = dayList;
        this.mResource = textResource;
        this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mDayList.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mDayList.get(position);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DayInfo day = mDayList.get(position);

        DayViewHolde dayViewHolder;

        if(convertView == null)
        {
            convertView = mLiInflater.inflate(mResource, null);

            if(position % 7 == 6)
            {
                convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP()+getRestCellWidthDP(), getCellHeightDP()));
            }
            else
            {
                convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP(), getCellHeightDP()));
            }


            dayViewHolder = new DayViewHolde();

            dayViewHolder.llBackground = (LinearLayout)convertView.findViewById(R.id.day_cell_ll_background);
            dayViewHolder.tvDay = (TextView) convertView.findViewById(R.id.day_cell_tv_day);

            convertView.setTag(dayViewHolder);
        }
        else
        {
            dayViewHolder = (DayViewHolde) convertView.getTag();
        }

        if(day != null)
        {
            dayViewHolder.tvDay.setText(day.getDay());

            if(day.isInMonth())
            {
                if(position % 7 == 0)
                {
                    dayViewHolder.tvDay.setTextColor(Color.RED);
                }
                else if(position % 7 == 6)
                {
                    dayViewHolder.tvDay.setTextColor(Color.BLUE);
                }
                else
                {
                    dayViewHolder.tvDay.setTextColor(Color.BLACK);
                }
            }
            else
            {
                dayViewHolder.tvDay.setTextColor(Color.GRAY);
            }

        }

        return convertView;
    }

    public class DayViewHolde
    {
        public LinearLayout llBackground;
        public TextView tvDay;

    }

    // 셀 길이 - 너비, 높이
    private int getCellWidthDP()
    {
//		int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int cellWidth = 940/7;
        //int cellWidth =

        return cellWidth;
    }

    private int getRestCellWidthDP()
    {
//		int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int cellWidth = 940%7;

        return cellWidth;
    }

    private int getCellHeightDP()
    {
//		int height = mContext.getResources().getDisplayMetrics().widthPixels;
        int cellHeight = 790/6;

        return cellHeight;
    }


}