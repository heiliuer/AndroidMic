package com.heiliuer.androidmic_server.udpmulti;

import android.util.Log;

import com.google.gson.Gson;

import java.util.TimerTask;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class MultiMyselfScheduler extends TimerTask {


    public static final String MSG_SEND_PREFER = "AndroidMic:";
    private MulticastInfo multicastInfo;
    private MulticastSenderConfigProvider provider;

    public MultiMyselfScheduler(MulticastSenderConfigProvider provider, MulticastInfo multicastInfo) {
        this.provider = provider;
        this.multicastInfo = multicastInfo;
    }

    @Override
    public void run() {
        Log.v("myLog", "begin tx:\n" + MSG_SEND_PREFER + new Gson().toJson(multicastInfo));
        UdpMulticastSender.beginSend(provider.getPort(), MSG_SEND_PREFER + new Gson().toJson(multicastInfo));
        Log.v("myLog", "complte tx");
    }
}
