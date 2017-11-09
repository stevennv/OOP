package com.example.admin.btloop.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.admin.btloop.R;
import com.example.admin.btloop.dialog.AddQuestionDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddQuestionActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLv1;
    private Button btnLv2;
    private Button btnLv3;
    private DatabaseReference mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        iniUI();
    }

    protected void iniUI() {
        mRoot = FirebaseDatabase.getInstance().getReference();
        btnLv1 = (Button) findViewById(R.id.btn_level_1);
        btnLv2 = (Button) findViewById(R.id.btn_level_2);
        btnLv3 = (Button) findViewById(R.id.btn_level_3);
        btnLv1.setOnClickListener(this);
        btnLv2.setOnClickListener(this);
        btnLv3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_level_1:
                AddQuestionDialog dialog1 = new AddQuestionDialog(this, android.R.style.Theme_Light, 1, mRoot, new AddQuestionDialog.checkOut() {
                    @Override
                    public void out() {
//                        finish();
                    }
                });
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.dialog_add_question);
                dialog1.show();
                break;
            case R.id.btn_level_2:
                AddQuestionDialog dialog = new AddQuestionDialog(this, android.R.style.Theme_Light, 2, mRoot, new AddQuestionDialog.checkOut() {
                    @Override
                    public void out() {
//                        finish();
                    }
                });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_add_question);
                dialog.show();
                break;
            case R.id.btn_level_3:
                AddQuestionDialog dialog2 = new AddQuestionDialog(this, android.R.style.Theme_Light, 3, mRoot, new AddQuestionDialog.checkOut() {
                    @Override
                    public void out() {
//                        finish();
                    }
                });
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(R.layout.dialog_add_question);
                dialog2.show();
                break;
        }
    }
}
