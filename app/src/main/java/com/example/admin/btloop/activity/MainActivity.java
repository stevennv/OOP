package com.example.admin.btloop.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.btloop.R;
import com.example.admin.btloop.dialog.UserInfo;
import com.example.admin.btloop.utils.SharedPreferencesUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnPlay;
    private Button btnAddQuestion;
    private Button btnLogin;
    private Button btnLogout;
    private Button btnFriends;
    private CallbackManager callbackManager;
    private SharedPreferencesUtils utils;
    private CircleImageView civAvatar;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        iniUI();
    }

    protected void iniUI() {
        utils = new SharedPreferencesUtils(this);
        callbackManager = CallbackManager.Factory.create();
        btnAddQuestion = (Button) findViewById(R.id.btn_add_question);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnLogin = (Button) findViewById(R.id.btn_login);
        civAvatar = (CircleImageView) findViewById(R.id.civ_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnFriends = (Button) findViewById(R.id.btn_friends);
        btnAddQuestion.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnFriends.setOnClickListener(this);
        checkLogin();
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
            case R.id.btn_login:
                loginFb();
                break;
            case R.id.btn_logout:
                logout();
                break;
            case R.id.btn_friends:
                Intent intent2 = new Intent(this, FriendsActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void loginFb() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
            /* handle the result */
                                String json = response.getRawResponse();
                                Gson gson = new Gson();
                                UserInfo userInfo = gson.fromJson(json, UserInfo.class);
                                String avatar = "http://graph.facebook.com/"
                                        + userInfo.getId() + "/picture?type=large";
                                Log.d("onCompleted:", "onCompleted: " + avatar);
                                userInfo.setAvatar(avatar);
                                utils.saveUserInfo(userInfo);
                                Glide.with(MainActivity.this).load(userInfo.getAvatar()).into(civAvatar);
                                tvName.setText(userInfo.getName());
                                btnLogin.setVisibility(View.GONE);
                                btnLogout.setVisibility(View.VISIBLE);
                            }
                        }
                ).executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void checkLogin() {
        if (AccessToken.getCurrentAccessToken() != null) {
            Glide.with(MainActivity.this).load(utils.getUserInfo().getAvatar()).into(civAvatar);
            tvName.setText(utils.getUserInfo().getName());
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        btnLogin.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.GONE);
        civAvatar.setVisibility(View.GONE);
        tvName.setVisibility(View.GONE);
    }
}
