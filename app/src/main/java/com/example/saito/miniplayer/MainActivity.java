package com.example.saito.miniplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* メディアボタンフィルター */
        IntentFilter mediaFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        mediaFilter.setPriority(1000000000);
        // BTレシーバ登録
        registerReceiver(headsetReceiver, mediaFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // BTレシーバ破棄
        unregisterReceiver(headsetReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
    /**
     * BTレシーバ
     */
    private final BroadcastReceiver headsetReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction))
                return;
            KeyEvent event = (KeyEvent) intent
                    .getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            int keycode = event.getKeyCode();
            int action = event.getAction();
            Log.i("keycode", String.valueOf(keycode));
            Log.i("action", String.valueOf(action));
            //onKeyDown(keyCode, event)
            if (keycode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                    || keycode == KeyEvent.KEYCODE_HEADSETHOOK) {
                if (action == KeyEvent.ACTION_DOWN) {
                    // 動画スタート
                    start();
                }
            }
            if (keycode == KeyEvent.KEYCODE_MEDIA_NEXT) {
                if (action == KeyEvent.ACTION_DOWN) {
                    // 次へ
                    nextPush();
                }
            }
            if (keycode == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
                if (action == KeyEvent.ACTION_DOWN) {
                    // 次へ
                    nextPush();
                }
            }
        }

    };
}
