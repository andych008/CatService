package com.founder.server;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;

import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * startForeground aidl的使用
 *
 * @author 喵叔catuncle
 * Created at 2017/9/29 3:50
 */
public class MyService extends Service {

    private IMyAidlInterface.Stub binder = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String reverse(String src) throws RemoteException {
            String dest = new StringBuilder(src).reverse().toString();
            Timber.d(" src = %s,  dest = %s", src, dest);
            return dest;
        }
    };


    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();

        // 在API11之后构建Notification的方式
        Notification.Builder builder = new Notification.Builder(this); //获取一个Notification构造器
        Intent nfIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("www.baidu.com"));

        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("下拉列表中的Title")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("要显示的内容")
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        Notification notification;
        if (Build.VERSION.SDK_INT < 16) {
            notification = builder.getNotification();
        } else {
            notification = builder.build();
        }
        notification.defaults = Notification.DEFAULT_ALL; //设置为默认的声音

        startForeground(1, notification);

    }

    @DebugLog
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}