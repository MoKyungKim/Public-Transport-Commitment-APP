package com.example.cooperativeproject2.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.cooperativeproject2.models.PlaceAPI;

import java.util.ArrayList;

public class PlaceAutoSuggestAdapter extends ArrayAdapter implements Filterable {

    ArrayList<String> results;

    int resource;
    Context context;

    PlaceAPI placeAPI=new PlaceAPI();

    public PlaceAutoSuggestAdapter(Context context, int resId){
        super(context,resId);
        this.context=context;
        this.resource=resId;
    }

    @Override
    public int getCount() {
        return results.size();
    }
    @Override
    public String getItem(int pos){
        return results.get(pos);
    }

    public Filter getFilter(){
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                if(constraint!=null){
                    results=placeAPI.autoComplete(constraint.toString());

                    filterResults.values=results;
                    filterResults.count=results.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results!=null && results.count>0){
                    notifyDataSetChanged();
                }
                else{
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

}
