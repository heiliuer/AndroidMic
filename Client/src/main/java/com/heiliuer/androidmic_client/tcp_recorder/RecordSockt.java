package com.heiliuer.androidmic_client.tcp_recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class RecordSockt extends Thread {


    private String socketIp;
    private AudioRecord recorder;
    private int socketPort;


    public RecordSockt(String socketIp, int socketPort) {
        super();

        this.socketIp = socketIp;
        this.socketPort = socketPort;


        int sampleRateInHz = 8000;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int audioSource = MediaRecorder.AudioSource.VOICE_COMMUNICATION;

//        int sampleRateInHz =6000;
//        int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
//        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
//        int audioSource = MediaRecorder.AudioSource.MIC;

        int min = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        recorder = new AudioRecord(audioSource, sampleRateInHz, channelConfig,
                audioFormat, min);
    }


    @Override
    public void run() {
        super.run();
        byte[] buffer = new byte[128];//越大，延时越大
        try {
            recorder.startRecording();
            Socket socket = new Socket(socketIp, socketPort);
            OutputStream outputStream = socket.getOutputStream();
            int loseDataCount = 10;//丢掉开头几个包，减少延时
            while ((!socket.isClosed() && socket.isBound())) {
                int size = recorder.read(buffer, 0, buffer.length);
                if (size != -1 && loseDataCount <= 0) {
                    //Log.v("myLog", "record read buffer size:" + size);
                    outputStream.write(buffer, 0, size);
                    outputStream.flush();
                } else {
                    loseDataCount--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            recorder.release();
        }

    }

    private boolean running = false;

    public void release() {
        running = false;
    }
}
