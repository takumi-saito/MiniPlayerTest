package com.example.saito.miniplayer;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;

public abstract class BaseActivity extends AppCompatActivity implements MediaController.MediaPlayerControl{

    private String TAG = BaseActivity.class.getSimpleName();

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    MiniPlayer mMiniPlayer;
    MediaController mMediaController;
    MediaController.MediaPlayerControl mMediaPlayerControl;

    ImageButton mBtnPlay;
    ImageButton mBtnBack;
    ImageButton mBtnNext;

    private DataApplication mApp;
    AudioManager mAudioManager;
//    ComponentName mRemoteControlResponder;
    BroadcastReceiver mMediaButtonReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 縦画面固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_mp);

        mApp = (DataApplication) getApplication();

        View root = findViewById(R.id.mini_player_view);

        mBtnBack = (ImageButton) findViewById(R.id.button_header_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnNext = (ImageButton) findViewById(R.id.button_header_next);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPush();
            }
        });

        mSurfaceView = (SurfaceView) findViewById(R.id.mini_player_surface_view);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(createSurfaceHolderCallback());

        mBtnPlay = (ImageButton) findViewById(R.id.mini_player_button);
        mBtnPlay.setBackgroundResource(R.drawable.btn_pause_selector);
        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        mAudioManager =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        mRemoteControlResponder = new ComponentName(getPackageName(), BroadcastReceiver.class.getCanonicalName());
        // AudioManager登録
//        mAudioManager.registerMediaButtonEventReceiver(mRemoteControlResponder);


    }

    /**
     * ステータスを見てボタンを変更
     */
    private void setBtnImage() {
        mBtnPlay.setBackgroundResource(mMediaPlayerControl.isPlaying() ? R.drawable.btn_pause_selector : R.drawable.btn_play_selector);
    }

    @Override
    protected void onStart() {
        mMiniPlayer = new MiniPlayer(getApplicationContext(), mSurfaceView, mApp);
        mMediaController = mMiniPlayer.getMediaController();
        mMediaPlayerControl = mMiniPlayer.getMediaPlayerControl();
        super.onStart();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        // バックグラウンド再生を許可する
//        requestVisibleBehind(true);
        // メディアボタンレシーバ
        IntentFilter mediaButtonFilter = new IntentFilter();
        mediaButtonFilter.addAction(Intent.ACTION_MEDIA_BUTTON);
        mMediaButtonReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        // メディアボタンレシーバ登録
        registerReceiver(mMediaButtonReceiver, mediaButtonFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // メディアボタンレシーバ破棄
//        unregisterReceiver(mMediaButtonReceiver);
    }

    @Override
    protected void onStop() {
        mMiniPlayer.destroy();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // AudioManager破棄
//        mAudioManager.unregisterMediaButtonEventReceiver(mRemoteControlResponder);

        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private SurfaceHolder.Callback createSurfaceHolderCallback() {
        SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.v(TAG, "surfaceCreated");
                // サーフェースの準備ができたら再生
                mMiniPlayer.load(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.v(TAG, "surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.v(TAG, "surfaceDestroyed");
                mMiniPlayer.destroy();
            }
        };

        return callback;
    }

    protected void nextPush() {
        mApp.setFilePath(mMiniPlayer.getFilePath());
        mApp.setCurrentPosition(mMediaPlayerControl.getCurrentPosition());
        Log.v(TAG, "mApp.filepath : " + mApp.getFilePath() + "  mApp.cp : " + mApp.getCurrentPosition());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(getApplicationContext(), SecondActivity.class);
        noAnimationStartActivity(intent);
    }

    /**
     * アニメーションなしActivity起動
     *
     * @param intent
     */
    public void noAnimationStartActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void start() {
        if (mMediaPlayerControl == null) {
            Log.v(TAG, "click is null mMediaPlayerControl");
            return;
        }
        if (mMediaPlayerControl.isPlaying()) {
            // 停止の場合
            mMediaPlayerControl.pause();
        } else {
            // 再生の場合
            mMediaPlayerControl.start();
        }
        setBtnImage();
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void pause() {
        if (mMediaPlayerControl == null) {
            Log.v(TAG, "click is null mMediaPlayerControl");
            return;
        }
        if (mMediaPlayerControl.isPlaying()) {
            // 停止の場合
            mMediaPlayerControl.pause();
        } else {
            // 再生の場合
            mMediaPlayerControl.start();
        }
        setBtnImage();
    }

}
