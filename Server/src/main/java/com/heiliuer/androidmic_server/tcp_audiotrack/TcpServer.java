package com.heiliuer.androidmic_server.tcp_audiotrack;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2016/4/28 0028.
 */
public class TcpServer extends Thread {

    private int port = 9999;

    public interface OnConnectStateChanged {
        void tcpClientConnected(Socket socket, ServerSocket serverSocket);

        void tcpException(Exception ex);
    }

    public TcpServer(int port, OnConnectStateChanged onConnnectStateChanged) {
        this.port = port;
        this.onConnectStateChanged = onConnnectStateChanged;
    }

    private OnConnectStateChanged onConnectStateChanged;

    public boolean running;

    public void release() {
        running = false;
    }

    @Override
    public void run() {
        super.run();
        running = true;
        try {
            ServerSocket socket = new ServerSocket(port);
            while (running) {
                Log.v("myLog", "accepting");
                Socket newSocket = socket.accept();
                Log.v("myLog", "accepted:ip=" + socket.getInetAddress().getHostAddress());
                if (onConnectStateChanged != null) {
                    onConnectStateChanged.tcpClientConnected(newSocket, socket);
                }

            }
        } catch (IOException e) {
            if (onConnectStateChanged != null) {
                onConnectStateChanged.tcpException(e);
            }
            e.printStackTrace();
        }
    }
}
