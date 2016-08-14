package com.app.ui.activity;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.app.ui.server.ToyVpnService2;
import com.app.ui.utile.DLog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_start_btn).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //如果vpn请求发起者这不是本app，会有一个intent返回
        Intent intent = VpnService.prepare(this);
        if (intent != null) {
            //启动intent
            startActivityForResult(intent, 0);
        } else {
            onActivityResult(0, RESULT_OK, null);
        }
    }

    protected void onActivityResult(int request, int result, Intent data) {
        //同意本app启动vpn服务
        if (result == RESULT_OK) {
            DLog.e("启动vpnServer", "===============");
            ToyVpnService2.startService(this);
            return;
        }
        DLog.e("不能启动vpnServer", "=============");
    }
}
