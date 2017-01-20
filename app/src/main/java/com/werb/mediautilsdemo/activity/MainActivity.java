package com.werb.mediautilsdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.werb.mediautilsdemo.R;

public class MainActivity extends AppCompatActivity {

    private Button audio,video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audio = (Button) findViewById(R.id.btn_audio);
        video = (Button) findViewById(R.id.btn_video);

        audio.setOnClickListener(audioClick);
        video.setOnClickListener(videoClick);
    }

    View.OnClickListener audioClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,AudioRecorderActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener videoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,VideoRecorderActivity.class);
            startActivity(intent);
        }
    };

}
