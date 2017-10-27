# TestJump

本项目主要是测试应用间的通信，通过三种方式：

- AIDL的形式进行应用通信；

- Scheme协议进行应用通信； 

- ContentProvider内容提供者；
## AIDL ##

AIDL是IPC方式中的一种，很好体现了Binder机制，

作用是通过在自己的APP里绑定一个其他APP的service，从而达到应用之间的交互；

#### A应用 ####

1.创建AIDL文件IMyService	，注意2个应用中的AIDL文件应该是一致的;

    interface IMyService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    	int add(in int value1, in int value2);
    	void registCallBack(NotifyCallBack callBack);
	}

注册了2个方法：

- 本地调用远程服务方法：add（in int value1, in int value2）具体实现让远程服务实现；
* 通过注册个接口registCallBack(NotifyCallBack callBack)让远程调用本地

2.绑定服务

首先创建一个Connection

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

通过bindService(i, connection, Context.BIND_AUTO_CREATE)

绑定远程服务服务

注意：*包名与类名别写错*

 	public void bindService() {
        connection = new MyConnection();
        Intent i = new Intent();
        i.setClassName("com.example.myapplication", "com.example.myapplication.MyService");
        boolean b = bindService(i, connection, Context.BIND_AUTO_CREATE);
        if (!b) {
            toast("未找到该服务！");
        }
    }

调用远程方法：

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

#### B应用 ####

创建MyService，并且返回一个Binder，该binder就可以实现自己的方法
 
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
最终返回binder给别人
		
	@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

这样AIDL的步骤基本就完成了，

但是想要很好的去理解Binder机制

我们根据AIDL生成的interface文件去学习

大致思想：

先写一个自己的接口MyInterface继承android.os.IInterface

定义自己要的方法

		public int add(int value1, int value2) throws android.os.RemoteException;
		public void registCallBack(com.example.myapplication.NotifyCallBack callBack) throws android.os.RemoteException;	

然后Proxy动态代理实现我们这个接口MyInterface

里面在具体实现自己的方法；

关键返回这个代理出去：

	Proxy(android.os.IBinder remote)
	{
	mRemote = remote;
	}
	@Override public android.os.IBinder asBinder()
	{
	return mRemote;
	}


## Scheme跳转 ##

Scheme跳转说白就是根据协议进行跳转；

URI的介绍:[http://blog.csdn.net/harvic880925/article/details/44679239](http://blog.csdn.net/harvic880925/article/details/44679239 "URI的介绍")

### 使用 ###

两种跳转形式：显示和隐示；

显示：

定义好Intent相关的action，type，以及传递数据

	     Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("xjr/text");
                intent.putExtra("text" , "我是显示跳转过来的");
                intent.setClassName("com.example.administrator.a.myapplication" ,
                        "com.example.administrator.myapplication.SchemeActivity");
                startActivity(intent);

隐示：

根据URI进行跳转

                Uri uri = Uri.parse("xjr://domain?text=我是隐示跳转过来的");
                Intent intent1 = new Intent();
                intent1.setData(uri);
                startActivity(intent1);

最后在B应用新建SchemeActivity

处理对应协议

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

别忘了还有XML文件

exported一定要设置为true，允许别的应用

	<activity android:name=".SchemeActivity"
            android:exported="true"
            android:launchMode="singleTask"
            >
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:scheme="xjr"/>
            </intent-filter>

        </activity>


## ContentProvider内容提供者 ##

思想是通过将数据保存在ContentProvider中达到共享数据

关键是靠Provider中的authorities找到同一个；


A应用：

新建自己的DogProvider，实现插入和查询方法


   	public static final Uri URI = Uri.parse("content://xjr.de.dog/dog");

	 @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
	//        LogUtils.log("insert : " + uri);
        String tableName = getTableName(uri);
        db.insert(tableName, null, values);
        return uri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
	//        LogUtils.log("query : " + uri);
        String tableName = getTableName(uri);
        Cursor cursor = db.query(tableName,null,
                null,null,null,null,null);
        return cursor;
    }

插入数据：
        getContentResolver().insert(DogProvider.URI , values);

B应用：
获取

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







