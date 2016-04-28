package com.heiliuer.myapplication;

import android.content.Context;
import android.media.AudioTrack;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class TcpAudioTrackHandler implements TcpServer.OnConnnectStateChanged {

    private Context context;


    public TcpAudioTrackHandler(Context context) {
        this.context = context;
    }

    private TcpServer tcpServer;

    private ArrayList<SocketAudioTrack> socketAudioTracks = new ArrayList<>();

    public void start() {
        tcpServer = new TcpServer(this);
        tcpServer.start();
    }

    public void stop() {
        tcpServer.release();
    }

    @Override
    public void tcpClientConnected(Socket socket, ServerSocket serverSocket) {

        SocketAudioTrack sock = new SocketAudioTrack(socket, new SocketAudioTrack.OnStateChanged() {
            @Override
            public void mediaPlayerStartBefore(AudioTrack audioTrack) {

            }

            @Override
            public void mediaPlayerFinished(AudioTrack audioTrack, int type) {

            }
        });
        sock.start();
    }

    @Override
    public void tcpException(Exception ex) {
    }
}
