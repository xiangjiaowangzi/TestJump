package com.example.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Lbin on 2017/9/27.
 */

public class MyService extends Service {

    String text1 = "开始";
    String text2 = "开始";

    private RemoteCallbackList<NotifyCallBack> callbackList = new RemoteCallbackList<>();

    Binder binder = new IMyService.Stub() {

        @Override
        public int add(int value1, int value2) throws RemoteException {
            Log.e("aa", " value1 + value2 " + value1 + value2);
            if (callbackList.getRegisteredCallbackCount() > 0) {
                notifyCallBack(text1,text2);
            }
            return value1 + value2;
        }

        @Override
        public void registCallBack(NotifyCallBack callBack) throws RemoteException {
            callbackList.register(callBack);
        }
    };

    private void notifyCallBack(String a, String b) {
        int len = callbackList.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                callbackList.getBroadcastItem(i).notifyCall(a, b);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        callbackList.finishBroadcast();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        text1 = intent.getStringExtra("text1");
        text2 = intent.getStringExtra("text2");
        notifyCallBack(text1, text2);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * @param context
     * @param text1
     * @param text2
     */
    public static void startService(Context context, String text1 , String text2) {
        Intent intent = new Intent(context, MyService.class);
        intent.putExtra("text1", text1);
        intent.putExtra("text2", text2);
        context.startService(intent);
    }
}
