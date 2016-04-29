package com.heiliuer.androidmic_server;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.heiliuer.androidmic_server.tcp_audiotrack.TcpAudioTrackHandler;
import com.heiliuer.androidmic_server.tcp_audiotrack.TcpAudioTrackHandlerConfigProvider;
import com.heiliuer.androidmic_server.udpmulti.MultiMyselfScheduler;
import com.heiliuer.androidmic_server.udpmulti.MulticastInfo;
import com.heiliuer.androidmic_server.udpmulti.MulticastSenderConfigProvider;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private int PORT_UDP_MULTI = 9998, PORT_TCP_TRACK = 9999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new TcpAudioTrackHandler(new TcpAudioTrackHandlerConfigProvider() {
            @Override
            public int getPort() {
                return PORT_TCP_TRACK;
            }
        }).start();

        showIp();

        scheduleMulticastInfoSend();
    }

    private void scheduleMulticastInfoSend() {

        MulticastInfo mInfo = new MulticastInfo(Build.MODEL, "我是AndroidMic服务器");
        mInfo.setRecordPort(PORT_TCP_TRACK);

        new Timer().schedule(new MultiMyselfScheduler(new MulticastSenderConfigProvider() {
            @Override
            public int getPort() {
                return PORT_UDP_MULTI;
            }
        }, mInfo), 1000, 3000);
    }

    private void showIp() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);

        setTitle(ip);
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }
}
