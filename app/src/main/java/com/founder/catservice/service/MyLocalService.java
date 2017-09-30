package com.founder.catservice.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.founder.catservice.page.LocalServiceActivity;
import com.founder.serverapi.ServerCons;

import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * 本地service可以这样绑定：<br/>
 * <pre>
 *     Intent bindIntent = new Intent(this, MyLocalService.class);
 *     bindService(bindIntent, connection, BIND_AUTO_CREATE);
 * </pre>
 *
 * @author 喵叔catuncle
 * Created at 2017/9/29 3:09
 */
public class MyLocalService extends Service {
    private MyBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        private Handler handler;
        private Callback callback;

        public void setHandler(Handler handler) {
            this.handler = handler;
        }

        public void setCallback(Callback callback) {
            this.callback = callback;
        }

        @DebugLog
        public void startDownload() {
            Timber.d(" begin... = %s",  Thread.currentThread().getName());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 执行具体的下载任务
                    for (int i=0; i<10; i++) {
                        try {
                            Thread.sleep(200L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        int progress = (i+1)*10;

                        Intent intent = new Intent(LocalServiceActivity.MsgReceiver.action);
//                        Intent intent = new Intent("unknow.action");//未注册的action是走不到onReceive的
                        intent.putExtra("progress", progress);
                        LocalBroadcastManager.getInstance(MyLocalService.this).sendBroadcast(intent);
                        if (handler != null) {
                            handler.sendMessage(handler.obtainMessage(ServerCons.MSG_SUM, progress, 0));
                        }

                        if (callback!=null) {
                            callback.handleMessage(progress);
                        }
                    }
                }
            }).start();
        }

    }

    public interface Callback {
        void handleMessage(int progress);
    }

    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @DebugLog
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @DebugLog
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @DebugLog
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @DebugLog
    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }
}