package com.example.admin.btloop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by Admin on 12/11/2017.
 */

public class DialogQuit extends Dialog {
    private int numberQuestion;

    public DialogQuit(@NonNull Context context, int numberQuestion) {
        super(context);
        this.numberQuestion = numberQuestion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
