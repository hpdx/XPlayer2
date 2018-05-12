package com.anbetter.xplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LiveDemoActivity.class));
            }
        });
        findViewById(R.id.btn_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LiveListActivity.class));
            }
        });

        findViewById(R.id.btn_03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoPath = "rtmp://live.hkstv.hk.lxdns.com/live/hks";

                Intent intent = new Intent(MainActivity.this, LiveRoomActivity.class);
                intent.putExtra("url", videoPath);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_06).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScrollingActivity.class));
            }
        });

        findViewById(R.id.btn_04).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, XPlayerActivity.class));
            }
        });

        findViewById(R.id.btn_05).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.btn_07).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
