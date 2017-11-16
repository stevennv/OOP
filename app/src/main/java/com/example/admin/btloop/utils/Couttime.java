package com.example.admin.btloop.utils;

import android.os.CountDownTimer;

/**
 * Created by Admin on 11/9/2017.
 */

public class Couttime extends CountDownTimer {
    private finish finish;
    private progress progress;

    public Couttime(long millisInFuture, long countDownInterval, finish finish, progress progress) {
        super(millisInFuture, countDownInterval);
        this.finish = finish;
        this.progress = progress;
    }

    @Override
    public void onTick(long l) {
        progress.execute((int) l / 1000);
    }

    @Override
    public void onFinish() {
        finish.finish();
    }

    public interface finish {
        void finish();
    }

    public interface progress {
        void execute(int time);
    }
}
