package com.heiliuer.androidmic;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String PATH_NAME = Environment.getExternalStorageDirectory() + "/AndroidMic/data.dat";

    static {
        //创建父文件夹
        File parent = new File(new File(PATH_NAME).getParent());
        parent.mkdirs();
    }


    private MediaRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(PATH_NAME);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                startRecord();
                break;
            case R.id.btn_stop:
                stopRecord();
                break;
        }
    }

    private void stopRecord() {
        recorder.stop();
        recorder.reset();   // You can reuse the object by going back to setAudioSource() step
        recorder.release(); // Now the object cannot be reused
        setTitle("");
    }

    private void startRecord() {
        try {
            recorder.prepare();
            recorder.start();   // Recording is now started
            setTitle("正在录音");
        } catch (IOException e) {
            Toast.makeText(this, "录音失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


}
