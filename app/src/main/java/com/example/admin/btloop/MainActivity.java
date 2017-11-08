package com.example.admin.btloop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.btloop.dialog.ConfirmQuitDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnAnswerA;
    private Button btnAnswerB;
    private Button btnAnswerC;
    private Button btnAnswerD;
    private TextView tvQuesttion;
    private ImageView imgSuggest1;
    private ImageView imgSuggest2;
    private DatabaseReference mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniUI();
    }

    protected void iniUI() {
        mRoot = FirebaseDatabase.getInstance().getReference();
        btnAnswerA = (Button) findViewById(R.id.btn_answer_a);
        btnAnswerB = (Button) findViewById(R.id.btn_answer_b);
        btnAnswerC = (Button) findViewById(R.id.btn_answer_c);
        btnAnswerD = (Button) findViewById(R.id.btn_answer_d);
        tvQuesttion = (TextView) findViewById(R.id.tv_question);
        imgSuggest1 = (ImageView) findViewById(R.id.img_suggest1);
        imgSuggest2 = (ImageView) findViewById(R.id.img_suggest2);
        btnAnswerA.setOnClickListener(this);
        btnAnswerB.setOnClickListener(this);
        btnAnswerC.setOnClickListener(this);
        btnAnswerD.setOnClickListener(this);
        imgSuggest1.setOnClickListener(this);
        imgSuggest2.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        ConfirmQuitDialog dialog = new ConfirmQuitDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_answer_a:
                break;
            case R.id.btn_answer_b:
                break;
            case R.id.btn_answer_c:
                break;
            case R.id.btn_answer_d:
                break;
            case R.id.img_suggest1:
                suggest1(3);
                break;
            case R.id.img_suggest2:
                setVisible();
                break;

        }
    }

    private void suggest1(int correct) {
//        imgSuggest1.setVisibility(View.INVISIBLE);
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        Random random = new Random();
        int numberSecond = randomNumberRange(4, 1, correct);
        list.remove(String.valueOf(correct));
        list.remove(String.valueOf(numberSecond));
        for (int i = 0; i < 2; i++) {
            setInvisible(Integer.parseInt(list.get(i)));
        }
    }

    public Integer randomNumberRange(int max, int min, int block) {
        Random rand = new Random();
        int randomNum;

        do {
            randomNum = rand.nextInt((max - min) + 1) + min;
        } while (randomNum == block && (max != block && block != min));

        return randomNum;
    }

    private void setInvisible(int number) {
        switch (number) {
            case 1:
                btnAnswerA.setVisibility(View.INVISIBLE);
                break;
            case 2:
                btnAnswerB.setVisibility(View.INVISIBLE);
                break;
            case 3:
                btnAnswerC.setVisibility(View.INVISIBLE);
                break;
            case 4:
                btnAnswerD.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void setVisible() {
        btnAnswerA.setVisibility(View.VISIBLE);
        btnAnswerB.setVisibility(View.VISIBLE);
        btnAnswerC.setVisibility(View.VISIBLE);
        btnAnswerD.setVisibility(View.VISIBLE);
    }
}
