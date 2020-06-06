package com.example.cooperativeproject2.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooperativeproject2.R;
import com.example.cooperativeproject2.fragments.Recent;
import com.example.cooperativeproject2.fragments.findfragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2017-08-07.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private findfragment mCtx;

    private List<Recent> items= null;
    private ArrayList<Recent> arrayList;

    public SearchAdapter(findfragment context, List<Recent> items) {
        this.mCtx=context;
        this.items=items;
        arrayList = new ArrayList<Recent>();
        arrayList.addAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_listview,null);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Recent item=items.get(position);

        holder.tv_address.setText(item.getAddress());
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
            for (Recent recent : arrayList) {
                String name = recent.getAddress();
                if (name.toLowerCase().contains(charText)) {
                    items.add(recent);
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