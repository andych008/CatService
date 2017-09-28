package com.founder.serverapi;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * http://sohailaziz05.blogspot.com/2012/05/intentservice-providing-data-back-to.html
 */
public class MyResultReceiver extends ResultReceiver {

    private Receiver receiver;

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    public MyResultReceiver(Handler handler) {
        super(handler);
    }

    public MyResultReceiver(Handler handler, Receiver receiver) {
        super(handler);
        this.receiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, resultData);
        }
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }
}
