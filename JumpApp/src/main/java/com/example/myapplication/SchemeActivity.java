package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by LiuB on 2017/10/27.
 */

public class SchemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shceme);
    }

    public void jump(View v){
        switch (v.getId()){
            case R.id.btn1 :
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("xjr/text");
                intent.putExtra("text" , "我是显示跳转过来的");
                intent.setClassName("com.example.administrator.a.myapplication" ,
                        "com.example.administrator.myapplication.SchemeActivity");
                startActivity(intent);
                break;
            case R.id.btn2 :
                Uri uri = Uri.parse("xjr://domain?text=我是隐示跳转过来的");
                Intent intent1 = new Intent();
                intent1.setData(uri);
                startActivity(intent1);
                break;
        }
    }
}
