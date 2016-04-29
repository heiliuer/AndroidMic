package com.heiliuer.androidmic_client.udpmulti;

import android.util.Log;

import com.google.gson.Gson;

import java.net.DatagramPacket;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class MulticastDeviceScanner implements UdpMulticastReceiver.OnStateListener {

    private OnScanComplteListener onScanComplteListener;

    private MulticastDeviceConfigProvider provider;

    public MulticastDeviceScanner(OnScanComplteListener onScanComplteListener, MulticastDeviceConfigProvider provider) {
        this.onScanComplteListener = onScanComplteListener;
        this.provider = provider;
    }

    public void startScan() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                UdpMulticastReceiver.beginReceive(provider.getPort(), MulticastDeviceScanner.this);
            }
        }.start();
    }

    ArrayList<MulticastInfo> multicastInfos = new ArrayList<>();


    public static final String MSG_SEND_PREFER = "AndroidMic:";

    @Override
    public void udpMulticastReceiverMsg(String msg, DatagramPacket dp) {
        Log.v("myLog", "rx:\n" + msg);
        if (msg != null) {
            msg = msg.trim();
            if (msg.startsWith(MSG_SEND_PREFER)) {
                msg = msg.replace(MSG_SEND_PREFER, "");
                MulticastInfo info = new Gson().fromJson(msg, MulticastInfo.class);
                info.setRecordIp(dp.getAddress().getHostAddress());
                multicastInfos.add(info);
                if (onScanComplteListener != null) {
                    onScanComplteListener.onScanADevice(info, multicastInfos);
                }
            }
        }
    }

    @Override
    public void udpMulticastReceiverException(Exception e) {
        if (onScanComplteListener != null) {
            onScanComplteListener.onScanFailed();
        }
    }

    public interface OnScanComplteListener {
        public void onScanADevice(MulticastInfo multicastInfo, ArrayList<MulticastInfo> multicastInfos);

        public void onScanFailed();
    }


}
