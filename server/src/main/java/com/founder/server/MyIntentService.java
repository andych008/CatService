package com.founder.server;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.founder.serverapi.ServerCons;

import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * IntentService的使用。通过Messenger或ResultReceiver或BroadcastReceiver三种方式返回结果
 */
public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ServerCons.ACTION_ADD.equals(action)) {
                final int param1 = intent.getIntExtra(ServerCons.EXTRA_PARAM1, 0);
                final int param2 = intent.getIntExtra(ServerCons.EXTRA_PARAM2, 0);
                Messenger messenger = null;
                if (intent.hasExtra(ServerCons.MESSENGER_TAG)) {
                    messenger = intent.getParcelableExtra(ServerCons.MESSENGER_TAG);
                }
                ResultReceiver resultReceiver = null;
                if (intent.hasExtra(ServerCons.RECEIVER_TAG)) {
                    resultReceiver = intent.getParcelableExtra(ServerCons.RECEIVER_TAG);
                }
                handleActionAdd(messenger, resultReceiver, param1, param2);
            }
        }
    }

    /**
     * Handle action Add in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAdd(Messenger messenger, ResultReceiver resultReceiver, int param1, int param2) {
        Timber.d("handleActionAdd--------------------%s, %s", param1, param2);
        //模拟耗时
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (messenger != null) {
            Message msg = Message.obtain();//返回给客户端的消息
            msg.what = ServerCons.MSG_SUM;
            msg.arg1 = param1 + param2;
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                Timber.e(e);
            }
        } else if (resultReceiver != null) {
            Bundle resultData = new Bundle();
            resultData.putInt(ServerCons.RESULT_TAG, param1 + param2);
            resultReceiver.send(0, resultData);
        } else {
            Intent intent = new Intent(ServerCons.ACTION_ADD);
//          Intent intent = new Intent("unknow.action");//未注册的action是走不到BroadcastReceiver.onReceive的
            intent.putExtra(ServerCons.RESULT_TAG, param1 + param2);
            sendBroadcast(intent);
        }
    }

    //-------------重写下面这些方法只是为了用hugo追踪方法调用日志--------------------
    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @DebugLog
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @DebugLog
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //--------------end----------------------------------------------------------
}
