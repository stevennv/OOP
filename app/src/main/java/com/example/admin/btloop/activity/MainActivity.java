package com.example.admin.btloop.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.admin.btloop.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnPlay;
    private Button btnAddQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniUI();
    }

    protected void iniUI() {
        btnAddQuestion = (Button) findViewById(R.id.btn_add_question);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnAddQuestion.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_question:
                Intent intent1 = new Intent(this, AddQuestionActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_play:
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                break;
        }
    }
}
