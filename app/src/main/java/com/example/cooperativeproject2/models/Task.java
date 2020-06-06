package com.example.cooperativeproject2.models;

import android.os.AsyncTask;
import android.util.Log;

import com.example.cooperativeproject2.fragments.mapfragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Task extends AsyncTask<String, Void, String> {

    String clientKey = "AIzaSyCiGa8w92IEGllhyyOHFLks6y_rweuObmg";
    private String str, receiveMsg;
    String place_id=((mapfragment) mapfragment.context).place_id;
    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL("https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&fields=name,rating,formatted_phone_number&key="+clientKey+"&language=ko;");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("x-waple-authorization", clientKey);

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg : ", receiveMsg);

                reader.close();
            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }
}