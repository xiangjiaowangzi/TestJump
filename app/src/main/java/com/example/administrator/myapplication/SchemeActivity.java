package com.example.administrator.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by LiuB on 2017/10/27.
 */

public class SchemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handler(intent);
    }

    /**
     * 处理intent协议
     *
     * @param intent
     */
    private void handler(Intent intent) {
        try {
            Uri data = intent.getData();
            String action = intent.getAction();
            String type = intent.getType();
            // 显示跳转
            if (action != null && action.equals(Intent.ACTION_SEND)
                    && type.equalsIgnoreCase("xjr/text")) {
                String text = intent.getStringExtra("text");
                MainActivity.launch(this, text);
            } else if (data != null) { //隐示跳转
                Uri schemeUri = data;
                String scheme = data.getScheme();
                if (scheme.startsWith("xjr")) {
                    String text = schemeUri.getQueryParameter("text");
                    MainActivity.launch(this, text);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "数据异常", Toast.LENGTH_LONG).show();
        } finally {
            finish();
        }
    }
}
