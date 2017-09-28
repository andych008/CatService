package com.founder.server;


import android.os.Message;
import android.os.RemoteException;

import com.founder.server.base.MessengerService;
import com.founder.serverapi.ServerCons;

/**
 * Messenger最常见用法
 *
 * @author 喵叔catuncle
 * Created at 2017/9/29 2:50
 */
public class MyMessengerService extends MessengerService {
    private static final int MSG_SUM = ServerCons.MSG_SUM;


    public MyMessengerService() {
        super("MyMessengerService");
    }

    @Override
    protected void onHandleParamMsg(Message paramMsg) {

        switch (paramMsg.what) {
            case MSG_SUM:
                try {
                    //模拟耗时
                    Thread.sleep(2000);
                    Message resultMsg = Message.obtain(null, MSG_SUM, paramMsg.arg1 + paramMsg.arg2, 0);
                    paramMsg.replyTo.send(resultMsg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}