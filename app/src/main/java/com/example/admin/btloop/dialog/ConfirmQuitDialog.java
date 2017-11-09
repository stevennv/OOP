package com.example.admin.btloop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.admin.btloop.R;

/**
 * Created by admin on 11/8/2017.
 */

public class ConfirmQuitDialog extends Dialog implements View.OnClickListener {
    private Button btnCancel;
    private Button btnOk;
    private clickQuit clickQuit;

    public ConfirmQuitDialog(Context context, clickQuit clickQuit) {
        super(context);
        this.clickQuit = clickQuit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_quit);
        iniUI();
    }

    private void iniUI() {
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                clickQuit.out();
                dismiss();
                break;
        }
    }

    public interface clickQuit {
        void out();
    }
}
