package com.example.cooperativeproject2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cooperativeproject2.Item;
import com.example.cooperativeproject2.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<Item> mPersons = new ArrayList<>();
    //private LayoutInflater mInflate;
    private Context mContext;



    public RecyclerViewAdapter(Context context, ArrayList<Item> persons) {
        this.mContext = context;
        //this.mInflate = LayoutInflater.from(context);
        this.mPersons = persons;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = mInflate.inflate(R.layout.recycler_item, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //데이터오 뷰를 바인딩
        //String url = mPersons.get(position).photo;
        holder.name.setText(mPersons.get(position).getName());
        holder.summary.setText(mPersons.get(position).getSummary());

    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }


    //ViewHolder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        //public ImageView imageView;
        public TextView summary;
        //ImageView search;

        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.maintext);
            //imageView = (ImageView) itemView.findViewById(R.id.imageView);
            summary = (TextView) view.findViewById(R.id.subtext);
            //search = (ImageView) itemView.findViewById(R.id.bt_search);
            //search.setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        }
    }
}