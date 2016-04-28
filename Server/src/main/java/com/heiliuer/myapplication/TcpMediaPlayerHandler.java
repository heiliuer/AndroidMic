package com.heiliuer.myapplication;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class TcpMediaPlayerHandler implements TcpServer.OnConnnectStateChanged {

    private Context context;

    private String mediaFilePath;

    public TcpMediaPlayerHandler(Context context) {
        this.context = context;
        mediaFilePath = context.getExternalFilesDir(null).getAbsolutePath();
        File file = new File(new File(mediaFilePath), "test");
        file.mkdirs();
    }

    private TcpServer tcpServer;

    private ArrayList<SocketMediaPlayer> socketMediaPlayers = new ArrayList<>();

    public void start() {
        tcpServer = new TcpServer(this);
        tcpServer.start();
    }

    public void stop() {
        tcpServer.release();
    }

    @Override
    public void tcpClientConnected(Socket socket, ServerSocket serverSocket) {

        SocketMediaPlayer socketMediaPlayer = new SocketMediaPlayer(mediaFilePath, socket, new SocketMediaPlayer.OnStateChanged() {
            @Override
            public void mediaPlayerStartBefore(MediaPlayer mediaPlayer) {

            }

            @Override
            public void mediaPlayerFinished(MediaPlayer mediaPlayer, int type) {

            }
        });
        socketMediaPlayer.start();
    }

    @Override
    public void tcpException(Exception ex) {

    }
}
