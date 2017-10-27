package com.example.administrator.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.IMyService;
import com.example.myapplication.NotifyCallBack;

public class MainActivity extends AppCompatActivity {

    public static final Uri URI = Uri.parse("content://xjr.de.dog/dog");

    IMyService iMyService;
    MyConnection connection;
    TextView textView1;
    TextView shcemeText;
    TextView providerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.text);
        textView1 = (TextView) findViewById(R.id.text2);
        shcemeText = (TextView) findViewById(R.id.scheme);
        providerText = (TextView) findViewById(R.id.provider);
        checkSheme();
        getProvider();
        Button button = (Button) findViewById(R.id.btn);
        Button button1 = (Button) findViewById(R.id.btn1);
        Button button2 = (Button) findViewById(R.id.btn2);
        final Button update = (Button) findViewById(R.id.update);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (iMyService != null) {
                        int a = iMyService.add(1, 2);
                        textView.setText("本地调用远程添加数字 : " + String.valueOf(a));
                    } else {
                        toast("未绑定服务！");
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
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProvider();
            }
        });
    }

    NotifyCallBack callBack = new NotifyCallBack.Stub() {
        @Override
        public void notifyCall(String a, String b) throws RemoteException {
            textView1.setText("更新远程信息 : " + a + b);
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
            toast("服务绑定成功！");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iMyService = null;
            toast("服务解绑！");
        }
    }

    public void bindService() {
        connection = new MyConnection();
        Intent i = new Intent();
        i.setClassName("com.example.myapplication", "com.example.myapplication.MyService");
        boolean b = bindService(i, connection, Context.BIND_AUTO_CREATE);
        if (!b) {
            toast("未找到该服务！");
        }
    }

    public void unBindService() {
        if (connection != null) {
            unbindService(connection);
            connection = null;
            iMyService = null;
        }
    }

    public void toast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    void checkSheme() {
        if (getIntent() != null) {
            String text = getIntent().getStringExtra("text");
            if (!TextUtils.isEmpty(text)) {
                shcemeText.setText("Scheme分享的数据 : " + text);
            }
        }
    }

    void getProvider() {
        // 获取游标
        Cursor cursor = getContentResolver().query(URI, null, null,
                null, null, null);
        if (cursor == null){
            providerText.setText(" cursor is null ");
            return;
        }
        StringBuffer sb = new StringBuffer();
        while (cursor.moveToNext()){
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            sb.append("一条"+year+"岁的"+name);
            sb.append("\n");
        }
        providerText.setText(sb.toString());
    }


    public static void launch(Context context, String text) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("text", text);
        context.startActivity(intent);
    }

}
