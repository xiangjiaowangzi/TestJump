package com.example.myapplication;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.helper.DogProvider;


/**
 * Created by LiuB on 2017/10/27.
 */

public class ProviderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
    }

    public void click(View v) {
        ContentValues values = new ContentValues();
        switch (v.getId()) {
            case R.id.btn:
                values.put("year" , 5);
                values.put("name" , "二哈");
                break;
            case R.id.btn1:
                values.put("year" , 2);
                values.put("name" , "柴犬");
                break;
            case R.id.btn2:
                values.put("year" , 4);
                values.put("name" , "泰迪");
                break;
        }
        getContentResolver().insert(DogProvider.URI , values);
        Toast.makeText(this , "插入一条数据" , Toast.LENGTH_LONG).show();
    }
}
