package com.werb.mediautilsdemo.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.werb.mediautilsdemo.MediaUtils;
import com.werb.mediautilsdemo.R;

import java.util.UUID;

/**
 * Created by wanbo on 2017/1/20.
 */

public class AudioRecorderActivity extends AppCompatActivity {

    private TextView mic,info;
    private ImageView micIcon;
    private MediaUtils mediaUtils;
    private boolean isCancel;
    private Chronometer chronometer;
    private RelativeLayout audioLayout;
    private String duration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        mediaUtils = new MediaUtils(this);
        mediaUtils.setRecorderType(MediaUtils.MEDIA_AUDIO);
        mediaUtils.setTargetDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
        mediaUtils.setTargetName(UUID.randomUUID() + ".m4a");
        // btn
        mic = (TextView) findViewById(R.id.tv_mic);
        info = (TextView) findViewById(R.id.tv_info);
        mic.setOnTouchListener(touchListener);
        chronometer = (Chronometer) findViewById(R.id.time_display);
        chronometer.setOnChronometerTickListener(tickListener);
        micIcon = (ImageView) findViewById(R.id.mic_icon);
        audioLayout = (RelativeLayout) findViewById(R.id.audio_layout);
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean ret = false;
            float downY = 0;
            int action = event.getAction();
            switch (v.getId()) {
                case R.id.tv_mic:
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            startAnim(true);
                            mediaUtils.record();
                            ret = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            stopAnim();
                            if (isCancel) {
                                isCancel = false;
                                mediaUtils.stopRecordUnSave();
                                Toast.makeText(AudioRecorderActivity.this, "取消保存", Toast.LENGTH_SHORT).show();
                            } else {
                                int duration = getDuration(chronometer.getText().toString());
                                switch (duration) {
                                    case -1:
                                        break;
                                    case -2:
                                        mediaUtils.stopRecordUnSave();
                                        Toast.makeText(AudioRecorderActivity.this, "时间太短", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        mediaUtils.stopRecordSave();
                                        String path = mediaUtils.getTargetFilePath();
                                        Toast.makeText(AudioRecorderActivity.this, "文件以保存至：" + path, Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float currentY = event.getY();
                            if (downY - currentY > 10) {
                                moveAnim();
                                isCancel = true;
                            } else {
                                isCancel = false;
                                startAnim(false);
                            }
                            break;
                    }
                    break;
            }
            return ret;
        }
    };

    Chronometer.OnChronometerTickListener tickListener = new Chronometer.OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            if (SystemClock.elapsedRealtime() - chronometer.getBase() > 60 * 1000) {
                stopAnim();
                mediaUtils.stopRecordSave();
                Toast.makeText(AudioRecorderActivity.this, "录音超时", Toast.LENGTH_SHORT).show();
                String path = mediaUtils.getTargetFilePath();
                Toast.makeText(AudioRecorderActivity.this, "文件以保存至：" + path, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private int getDuration(String str) {
        String a = str.substring(0, 1);
        String b = str.substring(1, 2);
        String c = str.substring(3, 4);
        String d = str.substring(4);
        if (a.equals("0") && b.equals("0")) {
            if (c.equals("0") && Integer.valueOf(d) < 1) {
                return -2;
            } else if (c.equals("0") && Integer.valueOf(d) > 1) {
                duration = d;
                return Integer.valueOf(d);
            } else {
                duration = c + d;
                return Integer.valueOf(c + d);
            }
        } else {
            duration = "60";
            return -1;
        }

    }

    private void startAnim(boolean isStart){
        audioLayout.setVisibility(View.VISIBLE);
        info.setText("上滑取消");
        mic.setBackground(getResources().getDrawable(R.drawable.mic_pressed_bg));
        micIcon.setBackground(null);
        micIcon.setBackground(getResources().getDrawable(R.drawable.ic_mic_white_24dp));
        if (isStart){
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.setFormat("%S");
            chronometer.start();
        }
    }

    private void stopAnim(){
        audioLayout.setVisibility(View.GONE);
        mic.setBackground(getResources().getDrawable(R.drawable.mic_bg));
        chronometer.stop();
    }

    private void moveAnim(){
        info.setText("松手取消");
        micIcon.setBackground(null);
        micIcon.setBackground(getResources().getDrawable(R.drawable.ic_undo_black_24dp));
    }
}
