package com.heiliuer.androidmic_server.tcp_audiotrack;

import android.media.AudioTrack;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class TcpAudioTrackHandler implements TcpServer.OnConnectStateChanged {


    private TcpServer tcpServer;

    private ArrayList<SocketAudioTrack> socketAudioTracks = new ArrayList<>();

    private TcpAudioTrackHandlerConfigProvider provider;

    public TcpAudioTrackHandler(TcpAudioTrackHandlerConfigProvider provider) {
        this.provider = provider;
    }

    public void start() {
        tcpServer = new TcpServer(provider.getPort(), this);
        tcpServer.start();
    }

    public void stop() {
        tcpServer.release();
        tcpServer = null;
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
