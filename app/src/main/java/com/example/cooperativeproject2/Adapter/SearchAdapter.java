package com.example.cooperativeproject2.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cooperativeproject2.R;
import com.example.cooperativeproject2.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017-08-07.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context mCtx;

    private List<object> items= null;
    private ArrayList<object> arrayList;

    public SearchAdapter(Context context, List<object> items) {
        this.mCtx=context;
        this.items=items;
        arrayList = new ArrayList<object>();
        arrayList.addAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_listview,null);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final object item=items.get(position);

        holder.tv_address.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length() == 0) {
            items.addAll(arrayList);
        } else {
            for (object object : arrayList) {
                String name = object.getName();
                if (name.toLowerCase().contains(charText)) {
                    items.add(object);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_address;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_address= (TextView) itemView.findViewById(R.id.findlabel);

        }


    }

}