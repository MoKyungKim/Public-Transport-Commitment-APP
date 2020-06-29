package com.example.cooperativeproject2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooperativeproject2.R;
import com.example.cooperativeproject2.itemitem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {


    Context mContext;
    private ArrayList<itemitem> arrayList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final itemitem text =arrayList.get(position);
        holder.tv.setText(text.getName());
    }


    public ItemAdapter(Context c, ArrayList<itemitem> items) {
        this.mContext = c;//보여지는 액티비티
        this.arrayList = items;

    }


    @Override
    public int getItemCount() { return this.arrayList.size(); }


    public  class ViewHolder extends RecyclerView.ViewHolder {
         TextView tv;

        public ViewHolder(View v) {
            super(v);
            tv = (TextView)v.findViewById(R.id.minju);
        }
    }


}
