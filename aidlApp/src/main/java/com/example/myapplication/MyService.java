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

    String a = "开始" ,b = "开始" ;

    private RemoteCallbackList<NotifyCallBack> callbackList = new RemoteCallbackList<>();


    Binder binder = new IMyService.Stub(){

        @Override
        public int add(int value1, int value2) throws RemoteException {
            Log.e("aa" ,  " value1 + value2 "  + value1 + value2 );
            if (callbackList.getRegisteredCallbackCount() > 0){
                notifyCallBack(a,b);
            }
            return value1 + value2 ;
        }

        @Override
        public void registCallBack(NotifyCallBack callBack) throws RemoteException {
            callbackList.register(callBack);
        }
    };

    private void notifyCallBack(String a , String b){
        int len = callbackList.beginBroadcast();
        for (int i = 0; i < len ; i ++){
            try {
                callbackList.getBroadcastItem(i).notifyCall(a , b);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        callbackList.finishBroadcast();
    }

    @Override
    public void onCreate() {
        Log.e("aa" ,  " onCreate ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        a = intent.getStringExtra("a");
        b = intent.getStringExtra("b");
        notifyCallBack(a,b);
        Log.e("aa" , " a " + a + " b " +b);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public static void startService(Context context , String a , String b){
        Intent intent = new Intent(context , MyService.class);
        intent.putExtra("a" , a);
        intent.putExtra("b" , b);
        context.startService(intent);
    }
}
