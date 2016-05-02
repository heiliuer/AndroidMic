package com.heiliuer.androidmic_server.tcp_audiotrack;

import android.annotation.TargetApi;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2016/4/28 0028.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SocketAudioTrack extends Thread {

    private Socket socket;
    private AudioTrack audioTrack;


    public interface OnStateChanged {
        int TYPE_EXCEPTION = 0x01;
        int TYPE_SOCKET_END = 0x02;

        void mediaPlayerStartBefore(AudioTrack audioTrack);

        void mediaPlayerFinished(AudioTrack audioTrack, int type);
    }


    private OnStateChanged onStateChanged;

    public SocketAudioTrack(Socket socket, OnStateChanged onStateChanged) {
        this.onStateChanged = onStateChanged;
        this.socket = socket;

        int sampleRateInHz = 8000;
        int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int streamType = AudioManager.MODE_IN_COMMUNICATION;
        int mode = AudioTrack.MODE_STREAM;

//        int sampleRateInHz = 6000;
//        int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
//        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
//        int streamType = AudioManager.STREAM_MUSIC;
//        int mode = AudioTrack.MODE_STREAM;


        int maxJitter = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        audioTrack = new AudioTrack(streamType, sampleRateInHz, channelConfig,
                audioFormat, maxJitter, mode);
    }

    @Override
    public void run() {
        super.run();
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024*16];
            int byteCount;
            audioTrack.play();
            // 启动音频设备，下面就可以真正开始音频数据的播放了

            while ((!socket.isClosed() && socket.isBound())) {
                byteCount = inputStream.read(buffer);
                if (byteCount != -1) {
                    audioTrack.write(buffer, 0, byteCount);
                    audioTrack.flush();
                    //Log.v("myLog", Arrays.toString(buffer));
                }
            }
            onStateChanged.mediaPlayerFinished(audioTrack, OnStateChanged.TYPE_SOCKET_END);
        } catch (IOException e) {
            e.printStackTrace();
            onStateChanged.mediaPlayerFinished(audioTrack, OnStateChanged.TYPE_EXCEPTION);
        }
        audioTrack.stop();
        audioTrack.release();
    }

}
