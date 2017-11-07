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

    public ConfirmQuitDialog(Context context) {
        super(context);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }
}
