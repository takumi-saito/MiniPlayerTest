package com.example.saito.miniplayer;

import android.app.Application;

/**
 * Created by saito on 2016/05/29.
 */
public class DataApplication extends Application {

    // 動画パス
    private String mFilePath;
    // 動画再生位置
    private int mCurrentPosition;

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int mCurrentPosition) {
        this.mCurrentPosition = mCurrentPosition;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }
}
