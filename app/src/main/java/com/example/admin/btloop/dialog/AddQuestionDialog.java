package com.example.admin.btloop.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.btloop.R;
import com.example.admin.btloop.model.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

/**
 * Created by Admin on 11/9/2017.
 */

public class AddQuestionDialog extends Dialog implements View.OnClickListener {
    private int level;
    private TextView tvLevel;
    private EditText edtQuestion;
    private EditText edtAnswerA;
    private EditText edtAnswerB;
    private EditText edtAnswerC;
    private EditText edtAnswerD;
    private Random random;
    private int correct;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String question;
    private DatabaseReference mRoot;
    private ImageView imgSend;
    private checkOut checkOut;

    public AddQuestionDialog(@NonNull Context context, int level, DatabaseReference mRoot) {
        super(context);
        this.level = level;
        this.mRoot = mRoot;
    }

    public AddQuestionDialog(@NonNull Context context, @StyleRes int themeResId, int level, DatabaseReference mRoot, checkOut checkOut) {
        super(context, themeResId);
        this.level = level;
        this.mRoot = mRoot;
        this.checkOut = checkOut;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_question);
        iniUI();
        creatQuestion();
    }

    private void iniUI() {
        mRoot = FirebaseDatabase.getInstance().getReference();
        tvLevel = findViewById(R.id.tv_level);
        edtAnswerA = findViewById(R.id.edt_answer_a);
        edtAnswerB = findViewById(R.id.edt_answer_b);
        edtAnswerC = findViewById(R.id.edt_answer_c);
        edtAnswerD = findViewById(R.id.edt_answer_d);
        edtQuestion = findViewById(R.id.edt_question);
        imgSend = findViewById(R.id.img_send);
        imgSend.setOnClickListener(this);
        if (level == 1) {
            tvLevel.setText(getContext().getString(R.string.level_1));
        } else if (level == 2) {
            tvLevel.setText(getContext().getString(R.string.level_2));
        } else {
            tvLevel.setText(getContext().getString(R.string.level_3));
        }
    }

    private void creatQuestion() {
        random = new Random();
        correct = random.nextInt(3) + 1;
        if (correct == 1) {
            edtAnswerA.setBackgroundResource(R.drawable.bg_btn_level);
            edtAnswerA.setHint(getContext().getString(R.string.enter_correct_answer));
        } else if (correct == 2) {
            edtAnswerB.setBackgroundResource(R.drawable.bg_btn_level);
            edtAnswerB.setHint(getContext().getString(R.string.enter_correct_answer));
        } else if (correct == 3) {
            edtAnswerC.setBackgroundResource(R.drawable.bg_btn_level);
            edtAnswerC.setHint(getContext().getString(R.string.enter_correct_answer));
        } else {
            edtAnswerD.setBackgroundResource(R.drawable.bg_btn_level);
            edtAnswerD.setHint(getContext().getString(R.string.enter_correct_answer));
        }
    }

    private void checkCondition() {
        answerA = edtAnswerA.getText().toString();
        answerB = edtAnswerB.getText().toString();
        answerC = edtAnswerC.getText().toString();
        answerD = edtAnswerD.getText().toString();
        question = edtQuestion.getText().toString();
        if (edtQuestion.getText().toString().equals("") || edtAnswerA.getText().toString().equals("")
                || edtAnswerB.getText().toString().equals("") || edtAnswerC.getText().toString().equals("")
                || edtAnswerD.getText().toString().equals("")) {
            Toast.makeText(getContext(), getContext().getString(R.string.enter_correct_answer), Toast.LENGTH_SHORT).show();
        } else {
            Question question1 = new Question(question, answerA, answerB, answerC, answerD, correct);
            String key = mRoot.child("Question").child("level_1").push().getKey();
            mRoot.child("Question").child("level_1").child(key).setValue(question1);
            checkOut.out();
            dismiss();
            Toast.makeText(getContext(), getContext().getString(R.string.send_success), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        checkCondition();
    }

    public interface checkOut {
        void out();
    }
}

