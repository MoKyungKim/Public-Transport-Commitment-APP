package com.example.cooperativeproject2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.cooperativeproject2.Adapter.ItemAdapter;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class itemactivity extends AppCompatActivity {


    private ArrayList<itemitem> item = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.itemlay);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleritem);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        ItemAdapter madapter = new ItemAdapter(this, item);
        recyclerView.setAdapter(madapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        madapter.notifyDataSetChanged();

        madapter.notifyDataSetChanged();

        Intent intent = getIntent();
        String[] traffic = intent.getStringArrayExtra("traffic");
        Log.d("qhwk", traffic[0]);

        for(int i = 0; i< traffic.length; i++) {

            String ii = traffic[i];
            item.add(new itemitem(ii));
            madapter.notifyDataSetChanged();
        }


    }





}

