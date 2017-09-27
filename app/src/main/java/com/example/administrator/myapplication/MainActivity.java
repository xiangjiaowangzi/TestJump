package com.example.administrator.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.IMyService;
import com.example.myapplication.NotifyCallBack;

public class MainActivity extends AppCompatActivity {

    IMyService iMyService ;
    MyConnection connection;
    TextView textView1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.text);
         textView1 = (TextView) findViewById(R.id.text2);
        Button button = (Button) findViewById(R.id.btn);
        Button button1 = (Button) findViewById(R.id.btn1);
        Button button2 = (Button) findViewById(R.id.btn2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (iMyService != null){
                        int a = iMyService.add(1,2);
                        textView.setText(String.valueOf(a));
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unBindService();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindService();
            }
        });
    }

    NotifyCallBack callBack = new NotifyCallBack.Stub() {
        @Override
        public void notifyCall(String a, String b) throws RemoteException {
            textView1.setText(a+b);
        }
    };


    class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iMyService = IMyService.Stub.asInterface(iBinder);
            try {
                iMyService.registCallBack(callBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, "Service connected", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iMyService = null;
            Toast.makeText(MainActivity.this, "Service disconnected", Toast.LENGTH_LONG).show();
        }
    }

    public void bindService(){
        connection = new MyConnection();
        Intent i = new Intent();
        i.setClassName("com.example.myapplication", "com.example.myapplication.MyService");
        Log.e("aa" , " 111 ");
        bindService(i, connection, Context.BIND_AUTO_CREATE);
        Log.e("aa" , " 222 ");
    }

    public void unBindService(){
        if (connection != null){
            unbindService(connection);
            connection = null;
            iMyService = null;
        }
    }

}
