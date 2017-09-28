package com.founder.catservice.page;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.founder.catservice.R;
import com.founder.catservice.databinding.ActivityMainBinding;

/**
 * singleTask适合作为程序入口点
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }


    public void onClick(View view) {
        switch(view.getId()) {
        case R.id.button6:

            startActivity(new Intent(this, ServiceActivity.class));
            break;
        case R.id.button7:

            startActivity(new Intent(this, LocalServiceActivity.class));
            break;
        case R.id.button8:

            startActivity(new Intent(this, MessengerServiceActivity.class));
            break;
        case R.id.button9:

            startActivity(new Intent(this, IntentServiceActivity.class));
            break;
        default:
            break;
        }
    }
}
