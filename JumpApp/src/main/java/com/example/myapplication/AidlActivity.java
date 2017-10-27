package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AidlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText1 = (EditText) findViewById(R.id.edit_1);
        final EditText editText2 = (EditText) findViewById(R.id.edit_2);

        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = editText1.getText().toString();
                String b = editText2.getText().toString();
                MyService.startService(AidlActivity.this , a , b);
            }
        });
    }
}
