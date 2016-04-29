package com.heiliuer.androidmic_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.heiliuer.androidmic_client.tcp_recorder.RecordSockt;
import com.heiliuer.androidmic_client.udpmulti.MulticastInfo;
import com.heiliuer.androidmic_client.udpmulti.ParceableMultiInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final int SCAN_REQUEST_CODE = 23;
    private RecordSockt recordSockt;
    private MulticastInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_scan).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                startRecord();
                break;
            case R.id.btn_stop:
                stopRecord();
                break;
            case R.id.btn_scan:
                startActivityForResult(new Intent(this, ScanActivity.class), SCAN_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SCAN_REQUEST_CODE) {
            ParceableMultiInfo infoDat = data.getParcelableExtra("multicastInfo");
            this.info = infoDat.getInfo();
            setTitle("已选：" + info.getName() + " " + info.getRecordIp() + ":" + info.getRecordPort());
        }
    }


    private void stopRecord() {
        if (recordSockt != null) {
            recordSockt.release();
            recordSockt = null;
        }
    }


    private void startRecord() {
        if (info == null) {
            Toast.makeText(this, "请先扫描设备", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Toast.makeText(MainActivity.this, "开始k歌", Toast.LENGTH_SHORT).show();
                recordSockt = new RecordSockt(info.getRecordIp(), info.getRecordPort());
                recordSockt.start();
            } catch (Exception e) {
                Toast.makeText(this, "k歌失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }


}
