package com.example.saito.miniplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;

import java.io.IOException;

/**
 * Created by saito on 2016/05/29.
 */
public class MiniPlayer {

    private String TAG = MiniPlayer.class.getSimpleName();

    private MediaController mMediaController;
    private MediaPlayer mMediaPlayer;
    private String mFilePath = "http://www.ajisaba.net/motion/dnld.php?fpath=penguin.mp4";
    private DataApplication mApp;

    // 外部ストレージ
    private static String[] VIDEO_PATHS = {
            "/Movies/testVideo.mp4",
            "/Movies/penguin.mp4",
    };

    public MediaController getMediaController() {
        return mMediaController;
    }

    public MediaController.MediaPlayerControl getMediaPlayerControl() {
        return mMediaPlayerControl;
    }

    public MiniPlayer(Context context, SurfaceView surfaceView, DataApplication application) {
        this.mMediaPlayer = initMediaPlayer();
        this.mMediaController = createMediaContolor(context, mMediaPlayer, surfaceView);
        mFilePath = Environment.getExternalStorageDirectory().toString() + VIDEO_PATHS[0];
        this.mApp = application;
    }

    public String getFilePath() {
        return mFilePath;
    }

    private MediaPlayer initMediaPlayer() {
        return initMediaPlayer(null);
    }

    public void load(SurfaceHolder surfaceHolder) {
        try {
            mMediaPlayer.setDataSource(mFilePath);
        } catch (IOException e) {
            Log.e(TAG, "load error", e);
        }
        mMediaPlayer.setDisplay(surfaceHolder);
        mMediaPlayer.prepareAsync();
    }

    public void destroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mMediaController != null) {
            mMediaController = null;
        }
        Log.d(TAG, "MediaPlayer destroyed");
    }

    /**
     * メディアプレイヤー初期処処置
     */
    private MediaPlayer initMediaPlayer(MediaPlayer mediaPlayer) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 再生準備完了時
                Log.v(TAG, "onPrepared");
                Log.v(TAG, "mApp.getFilePath() : " + mApp.getFilePath());
                Log.v(TAG, "mApp.getCurrentPosition() : " + mApp.getCurrentPosition());

                int currentPosition = mApp.getCurrentPosition();
                if (currentPosition > 0) {
                    Log.v(TAG, "currentPosition : " + currentPosition);
                    mp.seekTo(currentPosition);
                }
                mp.start();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 再生終了時
                mp.stop();
                mApp.setCurrentPosition(0);
            }
        });
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                //メディアプレイヤーの状態（詳細）
                return false;
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // エラー時
                return false;
            }
        });
        return mediaPlayer;
    }

    private MediaController createMediaContolor(Context context, final MediaPlayer mediaPlayer, SurfaceView surfaceView) {
        if (mediaPlayer != null) {
            return null;
        }
        if (surfaceView != null) {
            return null;
        }
        MediaController mediaController = new MediaController(context);
        mediaController.setMediaPlayer(mMediaPlayerControl);
        mediaController.setAnchorView(surfaceView);
        return mediaController;
    }

     private MediaController.MediaPlayerControl mMediaPlayerControl = new MediaController.MediaPlayerControl() {
        @Override
        public void start() {
            Log.v(TAG, "exec to start");

            mMediaPlayer.start();
        }

        @Override
        public void pause() {
            Log.v(TAG, "exec to pause");
            mMediaPlayer.pause();
        }

        @Override
        public int getDuration() {
            return mMediaPlayer.getDuration();
        }

        @Override
        public int getCurrentPosition() {
            return mMediaPlayer.getCurrentPosition();
        }

        @Override
        public void seekTo(int pos) {
            mMediaPlayer.seekTo(pos);
        }

        @Override
        public boolean isPlaying() {
            return mMediaPlayer.isPlaying();
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
    };
}
