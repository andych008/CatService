package com.founder.catservice.page;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.founder.catservice.R;
import com.founder.catservice.databinding.ActivityIntentServiceBinding;
import com.founder.serverapi.MyResultReceiver;
import com.founder.serverapi.MyStarter;
import com.founder.serverapi.ServerCons;

import java.lang.ref.WeakReference;

import hugo.weaving.DebugLog;

/**
 * IntentService的使用。通过Messenger或ResultReceiver或BroadcastReceiver三种方式返回结果
 *
 * @author 喵叔catuncle
 * Created at 2017/9/28 21:32
 */
public class IntentServiceActivity extends AppCompatActivity {

    private ActivityIntentServiceBinding binding;

    private TextView textView;
    private Button buttonView;
    private Button buttonView2;
    private Button buttonView3;

    private Messenger messenger;

    private MyResultReceiver resultReceiver;

    private BroadcastReceiver broadcastReceiver;

    private static class MyHandler extends Handler {
        private final WeakReference<IntentServiceActivity> reference;

        MyHandler(IntentServiceActivity activity) {
            super();
            reference = new WeakReference<>(activity);
        }

        @DebugLog
        @Override
        public void handleMessage(Message result) {
            switch (result.what) {
                case ServerCons.MSG_SUM:
                    IntentServiceActivity activity = reference.get();
                    if (activity != null) {
                        handleResult(activity.textView, activity.buttonView, result.arg1);
                    }
                    break;
            }
        }
    }


    private static class MyReceiver implements MyResultReceiver.Receiver {
        private final WeakReference<IntentServiceActivity> reference;

        MyReceiver(IntentServiceActivity activity) {
            super();
            reference = new WeakReference<>(activity);
        }

        @DebugLog
        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {
            IntentServiceActivity activity = reference.get();
            if (activity != null) {
                if (resultCode == 0) {
                    handleResult(activity.textView, activity.buttonView2, resultData.getInt(ServerCons.RESULT_TAG));
                } else {
                    handleResultFailed(activity.textView, activity.buttonView2);
                }

            }
        }
    }


    private static class MyBroadcastReceiver extends BroadcastReceiver {
        private final WeakReference<IntentServiceActivity> reference;

        MyBroadcastReceiver(IntentServiceActivity activity) {
            super();
            reference = new WeakReference<>(activity);
        }

        @DebugLog
        @Override
        public void onReceive(Context context, Intent intent) {
            IntentServiceActivity activity = reference.get();
            if (activity != null) {
                handleResult(activity.textView, activity.buttonView3, intent.getIntExtra(ServerCons.RESULT_TAG, 0));
            }
            //不需要下面的判断，因为注册广播receiver的时候有IntentFilter
//            if (ServerCons.ACTION_ADD.equals(intent.getAction())) {
//            }
        }

    }

    private static void handleResult(TextView textView, Button buttonView, int result) {
        textView.setText(textView.getText() + "=>" + result);
        buttonView.setEnabled(true);
    }

    private static void handleResultFailed(TextView textView, Button buttonView) {
        textView.setText(textView.getText() + "=>" + "error");
        buttonView.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intent_service);
        textView = binding.textView;
        buttonView = binding.button;
        buttonView2 = binding.button2;
        buttonView3 = binding.button3;

        messenger = new Messenger(new MyHandler(this));

        resultReceiver = new MyResultReceiver(new Handler(), new MyReceiver(this));

        //动态注册广播接收器  ----> 接收service的消息
        broadcastReceiver = new MyBroadcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServerCons.ACTION_ADD);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button: {
                int a = (int) (Math.random() * 10);
                int b = (int) (Math.random() * 10);
                MyStarter.startActionAdd(this, messenger, a, b);
                buttonView.setEnabled(false);
                textView.setText(a + " + " + b + " = calculating ...");
            }
            break;
            case R.id.button2: {
                int a = (int) (Math.random() * 10);
                int b = (int) (Math.random() * 10);
                MyStarter.startActionAdd(this, resultReceiver, a, b);
                buttonView2.setEnabled(false);
                textView.setText(a + " + " + b + " = calc ...");
            }
            break;
            case R.id.button3: {
                int a = (int) (Math.random() * 10);
                int b = (int) (Math.random() * 10);
                MyStarter.startActionAdd(this, a, b);
                buttonView3.setEnabled(false);
                textView.setText(a + " + " + b + " = calc ...");
            }
            break;
        }
    }

}
