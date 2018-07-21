package com.example.heath;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.example.heath.Model.AlarmModel;
import com.example.heath.Datebase.MyAlarmDataBase;
import com.example.heath.Main_Fragment.NormalFragment;
import com.example.heath.MyApplication;
import com.example.heath.R;


/**
 * 闹钟活动页面
 *
 */

public class PlayAlarmActivity extends AppCompatActivity {
    public static final String ALARM_ID = "id";

    private Vibrator vibrator;
    private String mWake;
    private NormalFragment normalFragment;

    private int mId;
    private String mRing;
    private MediaPlayer player;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_med);
        MyApplication.getInstance().addActivity(this);
        player =new MediaPlayer();
        //  调整闹钟声音大小
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (maxVolume/4)*3,
                AudioManager.FLAG_PLAY_SOUND);

        mId = Integer.parseInt(getIntent().getStringExtra(ALARM_ID));
        setmId(mId);

        MyAlarmDataBase db = new MyAlarmDataBase(this);
        AlarmModel am = db.getAlarm(mId);

        mWake = am.getWakeType();
        mRing = am.getRing();
        normalFragment = new NormalFragment();

        if (mWake.equals("常规")){
            initFragment(0);

        }

        if (mRing.equals("震动")){
            startVibrate();

        }else if (mRing.equals("响铃")) {
            SharedPreferences pf = getSharedPreferences("ringCode",MODE_PRIVATE);
            int ringCode  = pf.getInt("key_ring",1);

            startRing(ringCode);

        }else {
            SharedPreferences pf = getSharedPreferences("ringCode",MODE_PRIVATE);
            int ringCode  = pf.getInt("key_ring",1);
            startRing(ringCode);
            startVibrate();
        }

    }

    private void startRing(int ringCode) {
        switch (ringCode){
            case 1:
                if (player!=null && player.isPlaying()){
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this, R.raw.ring01);
                }else {
                    player = MediaPlayer.create(this,R.raw.ring01);

                }
                break;
            case 2:
                if (player!=null && player.isPlaying()){
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this,R.raw.ring02);
                }else {
                    player = MediaPlayer.create(this,R.raw.ring02);

                }
                break;
            case 3:
                if (player!=null && player.isPlaying()){
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this,R.raw.ring03);
                }else {
                    player = MediaPlayer.create(this,R.raw.ring03);

                }
                break;
            case 4:
                if (player!=null && player.isPlaying()){
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this,R.raw.ring04);
                }else {
                    player = MediaPlayer.create(this,R.raw.ring04);

                }
                break;
            case 5:
                if (player!=null && player.isPlaying()){
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this,R.raw.ring05);
                }else {
                    player = MediaPlayer.create(this,R.raw.ring05);

                }
                break;
            case 6:
                if (player!=null && player.isPlaying()){
                    player.stop();
                    player.release();
                    player = MediaPlayer.create(this,R.raw.ring06);
                }else {
                    player = MediaPlayer.create(this,R.raw.ring06);

                }
                break;

        }
        player.start();
        player.setLooping(true);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.start();
                player.setLooping(true);
            }
        });
    }

    private void startVibrate() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {1000,5000,1000,5000};
        vibrator.vibrate(pattern, 0);

    }

    private void initFragment(int i) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i){
            case 0:
                transaction.add(R.id.frag_content,normalFragment);
                transaction.show(normalFragment);
                break;


        }

        transaction.commit();
    }

    private void hideFragment(android.support.v4.app.FragmentTransaction transaction) {
        if (normalFragment != null){
            transaction.hide(normalFragment);
        }

    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmId(){
        return mId;
    }

    @Override
    protected void onDestroy() {
        if (player!=null){
            player.release();
        }
        if (vibrator!=null){
            vibrator.cancel();
        }

        super.onDestroy();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
