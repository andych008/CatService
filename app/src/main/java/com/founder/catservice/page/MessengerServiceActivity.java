package com.founder.catservice.page;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.founder.catservice.R;
import com.founder.catservice.databinding.ActivityServiceMsgBinding;
import com.founder.serverapi.MyStarter;
import com.founder.serverapi.ServerCons;

import java.lang.ref.WeakReference;

import hugo.weaving.DebugLog;

/**
 * Messenger最常见用法
 * <p>
 * onCreate bind service
 * onDestroy unbind
 *
 * @author 喵叔catuncle
 * Created at 2017/9/29 2:50
 */
public class MessengerServiceActivity extends AppCompatActivity {

    private static final int MSG_SUM = ServerCons.MSG_SUM;

    private ActivityServiceMsgBinding binding;

    private TextView textView;
    private Button buttonView;

    private Messenger serviceMessenger;

    private Messenger clientMessenger = new Messenger(new MyHandler(this));

    private static class MyHandler extends Handler {
        private final WeakReference<MessengerServiceActivity> reference;

        MyHandler(MessengerServiceActivity activity) {
            super();
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message resultMsg) {
            switch (resultMsg.what) {
                case MSG_SUM:
                    MessengerServiceActivity activity = reference.get();
                    if (activity != null) {
                        handleResult(activity.textView, activity.buttonView, resultMsg.arg1);
                    }
                    break;
            }
        }
    }


    private ServiceConnection connection = new ServiceConnection() {

        @DebugLog
        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceMessenger = null;
        }

        @DebugLog
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceMessenger = new Messenger(service);
        }
    };


    private static void handleResult(TextView textView, Button buttonView, int result) {
        textView.setText(textView.getText() + "=>" + result);
        buttonView.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_msg);
        textView = binding.textView;
        buttonView = binding.button;

        //绑定service  ----> 获取binder  ----->  发送消息给service
        MyStarter.bindActionPlus(this, connection);
    }


    @Override
    protected void onDestroy() {
        //!!!unbind
        unbindService(connection);
        super.onDestroy();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                int a = (int) (Math.random() * 100);
                int b = (int) (Math.random() * 100);

                Message paramMsg = Message.obtain(null, MSG_SUM, a, b);
                paramMsg.replyTo = clientMessenger;
                //往服务端发送消息
                try {
                    serviceMessenger.send(paramMsg);
                    buttonView.setEnabled(false);
                    textView.setText(a + " + " + b + " = calculating ...");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
