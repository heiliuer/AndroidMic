package com.heiliuer.androidmic_client.udpmulti;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Heiliuer on 2016/4/29 0029.
 */
public class UdpMulticastReceiver {

    public static void beginReceive(int port, OnStateListener onStateListener) {
        beginReceive(port, 5 * 1000, onStateListener);
    }

    private static final int length = 1024;

    public static void beginReceive(int port, int timeoutMS, OnStateListener onStateListener) {
        //接受组播和发送组播的数据报服务都要把组播地址添加进来
        String host = "225.0.0.1";//多播地址
        byte[] buf = new byte[length];
        MulticastSocket ms = null;
        DatagramPacket dp = null;
        StringBuffer sbuf = new StringBuffer();
        try {
            ms = new MulticastSocket(port);
            ms.setSoTimeout(timeoutMS);
            dp = new DatagramPacket(buf, length);

            //加入多播地址
            InetAddress group = InetAddress.getByName(host);
            ms.joinGroup(group);
            //监听多播端口打开
            ms.receive(dp);
            ms.close();
            String msg = new String(buf, 0, dp.getLength(), "UTF-8");
            if (onStateListener != null) {
                onStateListener.udpMulticastReceiverMsg(msg, dp);
            }
            // System.out.println("收到多播消息：" + data);
        } catch (IOException e) {
            e.printStackTrace();
            if (onStateListener != null) {
                onStateListener.udpMulticastReceiverException(e);
            }
        }
    }

    public interface OnStateListener {
        void udpMulticastReceiverMsg(String msg, DatagramPacket dp);

        void udpMulticastReceiverException(Exception e);
    }
}
