package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by LiuB on 2017/10/27.
 */

public class HomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onJump(View view){
        switch (view.getId()){
            case R.id.btn1:
                startActivity(new Intent(this , AidlActivity.class));
                break;
            case R.id.btn2 :
                startActivity(new Intent(this , SchemeActivity.class));
                break;
            case R.id.btn3:
                startActivity(new Intent(this , ProviderActivity.class));
                break;
        }
    }
}
