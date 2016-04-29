package com.heiliuer.androidmic_client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.heiliuer.androidmic_client.udpmulti.MulticastDeviceConfigProvider;
import com.heiliuer.androidmic_client.udpmulti.MulticastDeviceScanner;
import com.heiliuer.androidmic_client.udpmulti.MulticastInfo;
import com.heiliuer.androidmic_client.udpmulti.ParceableMultiInfo;

import java.util.ArrayList;

public class ScanActivity extends AppCompatActivity implements MulticastDeviceScanner.OnScanComplteListener {
    private int PORT_UDP_MULTI = 9998;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        new MulticastDeviceScanner(this, new MulticastDeviceConfigProvider() {
            @Override
            public int getPort() {
                return PORT_UDP_MULTI;
            }
        }).startScan();
    }

    @Override
    public void onScanADevice(final MulticastInfo multicastInfo, ArrayList<MulticastInfo> multicastInfos) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("multicastInfo", new ParceableMultiInfo(multicastInfo));
                //设置返回数据
                ScanActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                ScanActivity.this.finish();
            }
        });
    }

    @Override
    public void onScanFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ScanActivity.this, "扫描失败", Toast.LENGTH_SHORT).show();
                ScanActivity.this.finish();
            }
        });
    }
}
