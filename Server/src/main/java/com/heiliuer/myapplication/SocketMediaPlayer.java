package com.heiliuer.myapplication;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/28 0028.
 */
public class SocketMediaPlayer extends Thread {

    private Socket socket;
    private String mediaCacheFile;
    private MediaPlayer mediaPlayer = new MediaPlayer();


    public interface OnStateChanged {
        int TYPE_EXCEPTION = 0x01;
        int TYPE_SOCKET_END = 0x02;

        void mediaPlayerStartBefore(MediaPlayer mediaPlayer);


        void mediaPlayerFinished(MediaPlayer mediaPlayer, int type);
    }


    private OnStateChanged onStateChanged;

    public SocketMediaPlayer(String mediaCacheFilePath, Socket socket, OnStateChanged onStateChanged) {
        this.onStateChanged = onStateChanged;
        this.mediaCacheFile = mediaCacheFilePath + File.separator +
                new SimpleDateFormat("yyyyMMddhhmmss_" + System.currentTimeMillis()).format(new Date()) + ".wav";
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = new FileOutputStream(new File(mediaCacheFile));
            byte[] buffer = new byte[1024];
            int byteCount;

            while ((!socket.isClosed() && socket.isBound())) {
                byteCount = inputStream.read(buffer);
                if (byteCount != -1) {
                    outputStream.write(buffer, 0, byteCount);
                    Log.v("myLog", Arrays.toString(buffer));
                    if (!mediaStarted) {
                        mediaStarted = true;
                        ayscStartMediaPlayer();
                    }
                }
            }
            onStateChanged.mediaPlayerFinished(mediaPlayer, OnStateChanged.TYPE_SOCKET_END);
        } catch (IOException e) {
            e.printStackTrace();
            onStateChanged.mediaPlayerFinished(mediaPlayer, OnStateChanged.TYPE_EXCEPTION);
        }
    }

    private boolean mediaStarted = false;


    private void ayscStartMediaPlayer() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    mediaPlayer.setDataSource(mediaCacheFile);
                    mediaPlayer.prepare();
                    onStateChanged.mediaPlayerStartBefore(mediaPlayer);
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    mediaStarted = false;
                }
            }
        }.start();
    }

}
