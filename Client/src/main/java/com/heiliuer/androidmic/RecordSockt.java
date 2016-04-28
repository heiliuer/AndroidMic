package com.heiliuer.androidmic;

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
    private int socketPort;

    public RecordSockt(String socketIp, int socketPort) {
        super();
        this.socketIp = socketIp;
        this.socketPort = socketPort;

        int min = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, 8000, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, min);
    }


    /**
     * Audio config
     */
    private int sampleRate = 16000;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord recorder;


    @Override
    public void run() {
        super.run();
        byte[] buffer = new byte[1024];

        try {
            recorder.startRecording();
            Socket socket = new Socket(socketIp, socketPort);
            OutputStream outputStream = socket.getOutputStream();
            while ((!socket.isClosed() && socket.isBound())) {
                int size = recorder.read(buffer, 0, buffer.length);
                if (size != -1) {
                    outputStream.write(buffer, 0, size);
                }
            }
        } catch (Exception e) {
            // TODO
            System.out.println("Exception");
            recorder.release();
        }

    }

    private boolean running = false;

    public void release() {
        running = false;
    }
}
