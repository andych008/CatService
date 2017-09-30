package com.founder.catservice.page;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.founder.catservice.BuildConfig;
import com.founder.catservice.R;
import com.founder.catservice.databinding.ActivityServiceLocalBinding;
import com.founder.catservice.service.MyLocalService;
import com.founder.serverapi.ServerCons;

import java.lang.ref.WeakReference;

import hugo.weaving.DebugLog;

/**
 * LocalService的使用(binder   ----> localBroadCast或handler或回调函数)
 * onCreate 动态注册广播接收器
 * onDestroy unregisterReceiver
 *
 * onCreate bind service
 * onDestroy unbind
 */
public class LocalServiceActivity extends AppCompatActivity {

    private ActivityServiceLocalBinding binding;
    private MyLocalService.MyBinder myBinder;

    private LocalBroadcastManager bManager;
    private MsgReceiver msgReceiver;
    private Handler handler = new MyHandler(this);
    private ProgressBar progressBar;
    private TextView textView;

    private ServiceConnection connection = new ServiceConnection() {

        @DebugLog
        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBinder = null;
        }

        @DebugLog
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MyLocalService.MyBinder) service;
            myBinder.setHandler(handler);
            myBinder.setCallback(new MyLocalService.Callback() {
                @DebugLog
                @Override
                public void handleMessage(int progress) {
//                    Timber.d("Callback --- progress = %d", progress);
                }
            });
        }
    };

    public class MsgReceiver extends BroadcastReceiver {

        public static final String action = BuildConfig.APPLICATION_ID+".MsgReceiver";

        @DebugLog
        @Override
        public void onReceive(Context context, Intent intent) {
            //拿到进度，更新UI
//            int progress = intent.getIntExtra("progress", 0);
//            Timber.d("BroadcastReceiver --- progress = %d", progress);
        }

    }

    private static class MyHandler extends Handler {
        private final WeakReference<LocalServiceActivity> reference;

        MyHandler(LocalServiceActivity activity) {
            super();
            reference = new WeakReference<>(activity);
        }

        @DebugLog
        @Override
        public void handleMessage(Message resultMsg) {
            switch (resultMsg.what) {
                case ServerCons.MSG_SUM:
                    LocalServiceActivity activity = reference.get();
                    if (activity != null) {
                        //拿到进度，更新UI
                        int progress = resultMsg.arg1;
//                        Timber.d("Handler --- progress = %d", progress);
                        activity.progressBar.setProgress(progress);
                        activity.textView.setText(String.format("%d%%", progress));
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_local);
        progressBar = binding.progressBar;
        textView = binding.textView;
        initProgressUi();

        //动态注册广播接收器  ----> 接收service的消息
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MsgReceiver.action);
//        registerReceiver(msgReceiver, intentFilter);
        bManager = LocalBroadcastManager.getInstance(this);
        bManager.registerReceiver(msgReceiver, intentFilter);

        //绑定service  ----> 获取binder  ----->  发送消息给service
        Intent bindIntent = new Intent(this, MyLocalService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    private void initProgressUi() {
        progressBar.setMax(100);
        progressBar.setProgress(0);
        textView.setText(String.format("%d%%", 0));
    }

    @Override
    protected void onDestroy() {
        //!!!unbind
        unbindService(connection);

        //unregist
//        unregisterReceiver(msgReceiver);
        bManager.unregisterReceiver(msgReceiver);
        super.onDestroy();
    }

    public void onClick(View view) {
        switch(view.getId()) {

        case R.id.download:
            if (myBinder!=null) {
                myBinder.startDownload();
            }
            break;
        default:
            break;
        }
    }
}
