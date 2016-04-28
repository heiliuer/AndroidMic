package com.heiliuer.myapplication;

import android.annotation.TargetApi;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

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
        int maxJitter = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.MODE_IN_COMMUNICATION, 8000, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, maxJitter, AudioTrack.MODE_STREAM);
    }

    @Override
    public void run() {
        super.run();
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int byteCount;
            audioTrack.play(); // 启动音频设备，下面就可以真正开始音频数据的播放了

            while ((!socket.isClosed() && socket.isBound())) {
                byteCount = inputStream.read(buffer);
                if (byteCount != -1) {
                    audioTrack.write(buffer, 0, byteCount);
                    //outputStream.write(buffer, 0, byteCount);
                    Log.v("myLog", Arrays.toString(buffer));
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
