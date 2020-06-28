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

public class Task1 extends AsyncTask<String, Void, String> {

    String clientKey = "AIzaSyD3eueqNEbzZ6sj9bJVy1Ktr27lynFm1Gw";
    private String str, receiveMsg;
    String address=((mapfragment)mapfragment.context).addressName;
    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+address+"&key="+clientKey);

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