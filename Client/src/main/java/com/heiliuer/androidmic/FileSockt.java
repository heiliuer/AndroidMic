package com.heiliuer.androidmic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class FileSockt extends Thread {

    private File recordFile;

    private String socketIp;
    private int socketPort;

    public FileSockt(File recordFile, String socketIp, int socketPort) {
        super();
        this.recordFile = recordFile;
        this.socketIp = socketIp;
        this.socketPort = socketPort;
    }

    @Override
    public void run() {
        super.run();
        try {
            Socket socket = new Socket(socketIp, socketPort);
            InputStream inputStream = new FileInputStream(recordFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            OutputStream outputStream = socket.getOutputStream();
            while ((!socket.isClosed() && socket.isBound())) {
                byteCount = inputStream.read(buffer);
                if (byteCount != -1) {
                    outputStream.write(buffer, 0, byteCount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean running = false;

    public void release() {
        running = false;
    }
}
