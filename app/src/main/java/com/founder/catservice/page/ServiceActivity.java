package com.founder.catservice.page;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.founder.catservice.R;
import com.founder.catservice.databinding.ActivityServiceBinding;
import com.founder.server.IMyAidlInterface;
import com.founder.serverapi.MyStarter;

import hugo.weaving.DebugLog;


public class ServiceActivity extends AppCompatActivity {

    private ActivityServiceBinding binding;
    private IMyAidlInterface binder;

    private ServiceConnection connection = new ServiceConnection() {

        @DebugLog
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @DebugLog
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = IMyAidlInterface.Stub.asInterface(service);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_service);
        MyStarter.bindActionReverse(this, connection);
    }

    @Override
    protected void onDestroy() {
        //只能执行一次（即：和bind一一对应）
        unbindService(connection);
        super.onDestroy();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reverse:
                try {
                    String reverse = binder.reverse("abc");
                    Toast.makeText(this, reverse, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
