package com.heiliuer.androidmic_server.udpmulti;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * Created by Heiliuer on 2016/4/29 0029.
 */
public class UdpMulticastSender {
    public static boolean beginSend(int port, String message) {
        String host = "225.0.0.1";//多播地址
        try {
            InetAddress group = InetAddress.getByName(host);
            MulticastSocket s = new MulticastSocket();
            //加入多播组
            s.joinGroup(group);
            byte[] data = message.getBytes("UTF-8");
            DatagramPacket dp = new DatagramPacket(data, data.length, group, port);
            s.send(dp);
            s.close();
            //发送数据
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
