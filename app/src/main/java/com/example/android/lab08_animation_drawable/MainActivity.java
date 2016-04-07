package com.example.android.lab08_animation_drawable;

import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView m_img_duke;
    private AnimationDrawable m_frame_animation;
    private TextView m_tv_message;

    private View m_view_logo;
    private TextView m_logo_name;
    private TextView m_view_message;
    private Button m_btn_go;
    private SeekBar m_skb_duration;
    private TextView m_tv_duration;

    private TypedArray mNbaLogos;
    private int mNbaLogosCount;
    private String[] mNbaLogoNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFrameAnimation();
        initNbaLogos();
        initSeekBar();
    }

    private void initNbaLogos() {
        mNbaLogos = getNbaLogos();
        mNbaLogosCount = getNbaLogos().length();
        m_view_logo.setBackground(mNbaLogos.getDrawable(0));

        mNbaLogoNames = getNbaLogoNames();
    }

    private TypedArray getNbaLogos() {
        TypedArray logos = getResources().obtainTypedArray(R.array.nba_logos);
        return logos;
    }

    private String[] getNbaLogoNames() {
        String[] logonames = getResources().getStringArray(R.array.nba_logo_name);
        return logonames;
    }

    private void initView() {
        m_img_duke = (ImageView)findViewById(R.id.img_duke);
        m_tv_message = (TextView)findViewById(R.id.tv_message);

        m_view_logo = findViewById(R.id.view_logo);
        m_logo_name = (TextView)findViewById(R.id.tv_logo_name);
        m_view_message = (TextView)findViewById(R.id.view_message);

        m_btn_go = (Button)findViewById(R.id.btn_go);

        m_skb_duration = (SeekBar)findViewById(R.id.skb_duration);
        m_tv_duration = (TextView)findViewById(R.id.tv_duration);
    }

    private int mDuration;

    private void initSeekBar() {
        m_tv_duration.setText(String.valueOf(mDuration));
        m_skb_duration.setMax(20);
        m_skb_duration.setOnSeekBarChangeListener(new SeekBarClick());
    }

    private class SeekBarClick implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int duration = progress * 50;
            m_tv_duration.setText(String.valueOf(duration));
            mDuration = duration;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
//            m_tv_duration.setText("start");
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
//            m_tv_duration.setText("end");
        }
    }

    private void initFrameAnimation() {
        m_img_duke.setBackgroundResource(R.drawable.frame_animation);
        m_frame_animation = (AnimationDrawable)m_img_duke.getBackground();
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                m_frame_animation.start();
                break;
            case R.id.btn_stop:
                m_frame_animation.stop();
                break;
            case R.id.btn_5_secs:
                animation5secs();
                break;
        }
    }

    private Handler m_Handler = new Handler();

    private void animation5secs() {
        int delayMillis = 5 * 1000;
        Runnable task = new Task();
        boolean result = m_Handler.postDelayed(task, delayMillis);

        m_tv_message.setText(result ? "交付成功" : "交付失敗");
        m_frame_animation.start();
    }

    public void go(View view) {
        m_Handler.post(mStarRandomTask);
        m_Handler.postDelayed(mStopRandomTask, 20000);
        m_btn_go.setEnabled(false);
        m_btn_go.setText("選擇中...");
    }

    private StartRandomTask mStarRandomTask = new StartRandomTask();
    private StopRandomTask mStopRandomTask = new StopRandomTask();

    private class StartRandomTask implements Runnable {
        @Override
        public void run() {
            int index = (int)(Math.random() * mNbaLogosCount);
            m_view_logo.setBackground(mNbaLogos.getDrawable(index));
            m_logo_name.setText(mNbaLogoNames[index]);
            m_view_message.setText("post成功");
            m_Handler.postDelayed(this, mDuration);
        }
    }

    private class StopRandomTask implements Runnable {
        @Override
        public void run() {
            m_Handler.removeCallbacks(mStarRandomTask);
            m_btn_go.setEnabled(true);
            m_btn_go.setText("GO");
        }
    }

    private class Task implements Runnable {
        @Override
        public void run() {
            m_frame_animation.stop();
            m_tv_message.setText("時間到");
        }
    }
}
