package com.founder.server.base;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import hugo.weaving.DebugLog;

/**
 * 参考IntentService实现的MessengerService
 */
public abstract class MessengerService extends Service {

    private String name;
    private Messenger messenger;

    private final class ServiceHandler extends Handler {
        //looper 由 HandlerThread提供
        ServiceHandler(Looper looper) {
            super(looper);
        }

        @WorkerThread
        @Override
        public void handleMessage(Message paramMsg) {
            onHandleParamMsg(paramMsg);
        }
    }

    /**
     * Creates an MessengerService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MessengerService(String name) {
        super();
        this.name = name;
    }


    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("MessengerService[" + name + "]");
        thread.start();
        Handler handler = new ServiceHandler(thread.getLooper());
        messenger = new Messenger(handler);
    }

    @DebugLog
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @WorkerThread
    protected abstract void onHandleParamMsg(@Nullable Message paramMsg);
}