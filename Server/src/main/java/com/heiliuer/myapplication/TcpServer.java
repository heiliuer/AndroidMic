package com.heiliuer.myapplication;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2016/4/28 0028.
 */
public class TcpServer extends Thread {

    public static interface OnConnnectStateChanged {
        void tcpClientConnected(Socket socket, ServerSocket serverSocket);


        void tcpException(Exception ex);
    }

    public TcpServer(OnConnnectStateChanged onConnnectStateChanged) {
        this.onConnnectStateChanged = onConnnectStateChanged;
    }

    private OnConnnectStateChanged onConnnectStateChanged;

    public boolean running;

    public void release() {
        running = false;
    }

    @Override
    public void run() {
        super.run();
        running = true;
        try {
            ServerSocket socket = new ServerSocket(9999);
            while (running) {
                Log.v("myLog", "accepting");
                Socket newSocket = socket.accept();
                Log.v("myLog", "accepted:ip="+socket.getInetAddress().getHostAddress());
                if (onConnnectStateChanged != null) {
                    onConnnectStateChanged.tcpClientConnected(newSocket, socket);
                }

            }
        } catch (IOException e) {
            if (onConnnectStateChanged != null) {
                onConnnectStateChanged.tcpException(e);
            }
            e.printStackTrace();
        }


    }
}
