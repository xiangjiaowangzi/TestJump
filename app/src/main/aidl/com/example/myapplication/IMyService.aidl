// IMyService.aidl
package com.example.myapplication;
import com.example.myapplication.NotifyCallBack;
// Declare any non-default types here with import statements

interface IMyService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    int add(in int value1, in int value2);

        void registCallBack(NotifyCallBack callBack);

}
