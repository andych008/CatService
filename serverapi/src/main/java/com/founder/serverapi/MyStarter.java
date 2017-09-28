package com.founder.serverapi;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Messenger;
import android.os.ResultReceiver;

/**
 * 供客户端调用服务的工具类<br>
 * 应用外调用服务(跨包、cross-package)
 * https://stackoverflow.com/questions/5743485/android-resultreceiver-across-packages
 */
public class MyStarter {

    private static final String PKG = "com.founder.server";

    /**
     * action : add
     */
    public static void startActionAdd(Context context, Messenger messenger, int param1, int param2) {
        Intent intent = new Intent();
        intent.setPackage(PKG);
        intent.setAction(ServerCons.ACTION_ADD);
        intent.putExtra(ServerCons.MESSENGER_TAG, messenger);
        intent.putExtra(ServerCons.EXTRA_PARAM1, param1);
        intent.putExtra(ServerCons.EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * action : add
     */
    public static void startActionAdd(Context context, ResultReceiver resultReceiver, int param1, int param2) {
        Intent intent = new Intent();
        intent.setPackage(PKG);
        intent.setAction(ServerCons.ACTION_ADD);
        intent.putExtra(ServerCons.RECEIVER_TAG, resultReceiver);
        intent.putExtra(ServerCons.EXTRA_PARAM1, param1);
        intent.putExtra(ServerCons.EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * action : add
     */
    public static void startActionAdd(Context context, int param1, int param2) {
        Intent intent = new Intent();
        intent.setPackage(PKG);
        intent.setAction(ServerCons.ACTION_ADD);
        intent.putExtra(ServerCons.EXTRA_PARAM1, param1);
        intent.putExtra(ServerCons.EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * action : plus
     */
    public static void bindActionPlus(Context context, ServiceConnection connection) {
        Intent intent = new Intent();
        intent.setPackage(PKG);
        intent.setAction(ServerCons.ACTION_PLUS);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    /**
     * action : reverse
     */
    public static void bindActionReverse(Context context, ServiceConnection connection) {
        Intent intent = new Intent();
        intent.setPackage(PKG);
        intent.setAction(ServerCons.ACTION_REVERSE);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
}
