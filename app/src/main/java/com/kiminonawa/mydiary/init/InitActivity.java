package com.kiminonawa.mydiary.init;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.kiminonawa.mydiary.BuildConfig;
import com.kiminonawa.mydiary.R;
import com.kiminonawa.mydiary.entries.DiaryActivity;
import com.kiminonawa.mydiary.shared.SPFManager;


public class InitActivity extends Activity implements InitTask.InitCallBack {


    // 초기화 시간 (로고를 띄운뒤에 대기시간)
    private int initTime = 2000; // 3S
    private Handler initHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        initHandler = new Handler();
    }

    // 창이 꺼지지않고 백그라운드로 넘어갔다가 실행될 때
    @Override
    protected void onResume() {
        super.onResume();
        // initTime 만큼 지연된 후 동작
        initHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new InitTask(InitActivity.this, InitActivity.this).execute();
            }
        },initTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        initHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onInitCompiled(boolean showReleaseNote) {
            Intent DiaryIntent = new Intent(InitActivity.this, DiaryActivity.class);
            finish();
            InitActivity.this.startActivity(DiaryIntent);
    }
}
