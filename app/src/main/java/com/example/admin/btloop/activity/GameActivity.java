package com.example.admin.btloop.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.btloop.R;
import com.example.admin.btloop.dialog.ConfirmQuitDialog;
import com.example.admin.btloop.model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.view.Surface.ROTATION_270;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnAnswerA;
    private Button btnAnswerB;
    private Button btnAnswerC;
    private Button btnAnswerD;
    private TextView tvQuesttion;
    private ImageView imgSuggest1;
    private ImageView imgSuggest2;
    private ImageView imgSuggest3;
    private DatabaseReference mRoot;
    private List<Question> listLv1 = new ArrayList<>();
    private List<Question> listLv2 = new ArrayList<>();
    private List<Question> listLv3 = new ArrayList<>();
    private int numberQuestion = 0;
    private int correct;
    private int myAnswer;
    private ImageView imgMinion;
    private RelativeLayout rlMinion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        iniUI();
        getQuestionGame();
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
        imgSuggest3 = (ImageView) findViewById(R.id.img_suggest3);
        imgMinion = (ImageView) findViewById(R.id.img_minion);
        rlMinion = (RelativeLayout) findViewById(R.id.rl_minion);
        btnAnswerA.setOnClickListener(this);
        btnAnswerB.setOnClickListener(this);
        btnAnswerC.setOnClickListener(this);
        btnAnswerD.setOnClickListener(this);
        imgSuggest1.setOnClickListener(this);
        imgSuggest2.setOnClickListener(this);
        imgSuggest3.setOnClickListener(this);
        disableButton();

    }

    @Override
    public void onBackPressed() {
        ConfirmQuitDialog dialog = new ConfirmQuitDialog(this, new ConfirmQuitDialog.clickQuit() {
            @Override
            public void out() {
                finish();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_answer_a:
                myAnswer = 1;
                checkAnswer(myAnswer);
                break;
            case R.id.btn_answer_b:
                myAnswer = 2;
                checkAnswer(myAnswer);
                break;
            case R.id.btn_answer_c:
                myAnswer = 3;
                checkAnswer(myAnswer);
                break;
            case R.id.btn_answer_d:
                myAnswer = 4;
                checkAnswer(myAnswer);
                break;
            case R.id.img_suggest1:
                suggest1(correct);
                break;
            case R.id.img_suggest2:
                animationMinion();
                break;
            case R.id.img_suggest3:
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

    private void disableButton() {
        btnAnswerA.setClickable(false);
        btnAnswerB.setClickable(false);
        btnAnswerC.setClickable(false);
        btnAnswerD.setClickable(false);
    }

    private void enableButton() {
        btnAnswerA.setClickable(true);
        btnAnswerB.setClickable(true);
        btnAnswerC.setClickable(true);
        btnAnswerD.setClickable(true);
    }


    private void getQuestionGame() {
        mRoot.child("Question").child("level_1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listLv1.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    Log.d("onDataChange", "onDataChange: " + question.getQuestion() + "\n" + question.getCorrect());
                    listLv1.add(question);
                }
//                Collections.shuffle(listLv1);
//                tvQuesttion.setText(listLv1.get(0).getQuestion());
//                Log.d("onDataChange", "onDataChange: " + listLv1.get(1).getCorrect());
                setData(numberQuestion);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRoot.child("Question").child("level_2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listLv2.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    Log.d("onDataChange", "onDataChange: " + question.getQuestion() + "\n" + question.getCorrect());
                    listLv2.add(question);
                }
                Collections.shuffle(listLv2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRoot.child("Question").child("level_3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listLv3.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    Log.d("onDataChange", "onDataChange: " + question.getQuestion() + "\n" + question.getCorrect());
                    listLv3.add(question);
                }
                Collections.shuffle(listLv3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        setData(numberQuestion);
    }

    private void setData(int questionNumber) {
        setVisible();
        if (numberQuestion >= 0 && numberQuestion < 5) {
            Question question1 = listLv1.get(questionNumber);
            tvQuesttion.setText(question1.getQuestion());
            btnAnswerA.setText(question1.getAnswerA());
            btnAnswerB.setText(question1.getAnswerB());
            btnAnswerC.setText(question1.getAnswerC());
            btnAnswerD.setText(question1.getAnswerD());
            correct = question1.getCorrect();
        } else if (numberQuestion >= 5 && numberQuestion < 10) {
            Question question2 = listLv2.get(numberQuestion - 5);
            tvQuesttion.setText(question2.getQuestion());
            btnAnswerA.setText(question2.getAnswerA());
            btnAnswerB.setText(question2.getAnswerB());
            btnAnswerC.setText(question2.getAnswerC());
            btnAnswerD.setText(question2.getAnswerD());
            correct = question2.getCorrect();
        } else if (numberQuestion >= 10 && numberQuestion < 15) {
            Question question3 = listLv3.get(numberQuestion - 10);
            tvQuesttion.setText(question3.getQuestion());
            btnAnswerA.setText(question3.getAnswerA());
            btnAnswerB.setText(question3.getAnswerB());
            btnAnswerC.setText(question3.getAnswerC());
            btnAnswerD.setText(question3.getAnswerD());
            correct = question3.getCorrect();
        } else {
            endGame();
        }
        enableButton();
        Log.d("setData", "setData: " + numberQuestion);
    }

    private void checkAnswer(int myAnswer) {
//        disableButton();
        if (myAnswer == correct) {
//            Toast.makeText(getApplicationContext(), "Đúng", Toast.LENGTH_SHORT).show();
            numberQuestion++;
            setData(numberQuestion);
        } else {
            Toast.makeText(getApplicationContext(), "Sai", Toast.LENGTH_SHORT).show();
        }
    }

    private void endGame() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Chúc mừng bạn đã vượt qua 15 câu hỏi")
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    private void animationMinion() {
        rlMinion.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(0f, 0f, 0f, getDisplayHeight());
        animation.setStartOffset(500);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        animation.setInterpolator(new BounceInterpolator());
        animation.setInterpolator(getApplicationContext(), android.R.anim.bounce_interpolator);
        rlMinion.startAnimation(animation);
    }

    private int getDisplayHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int rotation = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();

        return rotation;
    }

}
