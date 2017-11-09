package com.example.admin.btloop.utils;

import android.os.CountDownTimer;

/**
 * Created by Admin on 11/9/2017.
 */

public class Couttime extends CountDownTimer {
    private finish finish;

    public Couttime(long millisInFuture, long countDownInterval, finish finish) {
        super(millisInFuture, countDownInterval);
        this.finish = finish;
    }

    @Override
    public void onTick(long l) {

    }

    @Override
    public void onFinish() {
        finish.finish();
    }

    public interface finish {
        void finish();
    }
}
