package com.example.cooperativeproject2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.cooperativeproject2.AWS.AuthenticationActivity;

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading); //xml , java 소스 연결
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent (getApplicationContext(), AuthenticationActivity.class);
                startActivity(intent); //다음화면으로 넘어감
                finish();
            }
        },3000); //3초 뒤에 Runner객체 실행하도록 함
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
