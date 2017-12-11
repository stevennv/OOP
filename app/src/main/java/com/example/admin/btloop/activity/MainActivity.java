package com.example.admin.btloop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.btloop.R;
import com.example.admin.btloop.dialog.NotificationDialog;
import com.example.admin.btloop.dialog.UserInfo;
import com.example.admin.btloop.model.Noti;
import com.example.admin.btloop.service.ChatHeadService;
import com.example.admin.btloop.service.FloatingViewService;
import com.example.admin.btloop.utils.Common;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.txusballesteros.bubbles.BubblesManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnPlay;
    private Button btnAddQuestion;
    private Button btnLogin;
    private Button btnLogout;
    private Button btnFriends;
    private Button btnWidget;
    private TextView tvCoint;
    private CallbackManager callbackManager;
    private SharedPreferencesUtils utils;
    private CircleImageView civAvatar;
    private TextView tvName;
    private DatabaseReference mRoot;
    private TextView tvNoti;
    private RelativeLayout rlNoti;
    private ValueEventListener listenerNoti;
    private ValueEventListener listenerAccept;
    private List<Noti> list = new ArrayList<>();
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private int myPoint;
    private int rivalPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        iniUI();
    }

    protected void iniUI() {
        mRoot = FirebaseDatabase.getInstance().getReference();
        utils = new SharedPreferencesUtils(this);
        callbackManager = CallbackManager.Factory.create();
        btnAddQuestion = (Button) findViewById(R.id.btn_add_question);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnLogin = (Button) findViewById(R.id.btn_login);
        civAvatar = (CircleImageView) findViewById(R.id.civ_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnFriends = (Button) findViewById(R.id.btn_friends);
        tvNoti = (TextView) findViewById(R.id.tv_noti);
        rlNoti = (RelativeLayout) findViewById(R.id.rl_noti);
        btnWidget = (Button) findViewById(R.id.btn_widget);
        tvCoint = (TextView) findViewById(R.id.tv_coint);
        btnWidget.setOnClickListener(this);
        btnAddQuestion.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnFriends.setOnClickListener(this);
        rlNoti.setOnClickListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLogin();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_question:
                Intent intent1 = new Intent(this, AddQuestionActivity.class);
                if (listenerNoti != null) {
                    mRoot.child("Home").child(utils.getUserInfo().getId()).child("Invite").removeEventListener(listenerNoti);
                }
                startActivity(intent1);
                break;
            case R.id.btn_play:
                Intent intent = new Intent(this, GameActivity.class);
                if (listenerNoti != null) {
                    mRoot.child("Home").child(utils.getUserInfo().getId()).child("Invite").removeEventListener(listenerNoti);
                }
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
//                mRoot.child("Home").child(utils.getUserInfo().getId()).child("Invite").removeEventListener(listenerNoti);
                startActivity(intent2);
                break;
            case R.id.rl_noti:
                NotificationDialog dialog = new NotificationDialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_notification);
                dialog.show();
                break;
            case R.id.btn_widget:
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
//
//
//                    //If the draw over permission is not available open the settings screen
//                    //to grant the permission.
//                    Intent intent3 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                            Uri.parse("package:" + getPackageName()));
//                    startActivityForResult(intent3, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
//                } else {
//                    startService(new Intent(MainActivity.this, FloatingViewService.class));
//                    finish();
//                }
                Intent intent12 = new Intent(MainActivity.this, ChatHeadService.class);
                intent12.putExtra("title", utils.getUserInfo().getAvatar());
                intent12.putExtra("text", utils.getUserInfo().getName());
                startService(intent12);
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
                                final UserInfo userInfo = gson.fromJson(json, UserInfo.class);
                                String avatar = "http://graph.facebook.com/"
                                        + userInfo.getId() + "/picture?type=large";
                                Log.d("onCompleted:", "onCompleted: " + avatar);
                                userInfo.setAvatar(avatar);
                                utils.saveUserInfo(userInfo);
                                Glide.with(MainActivity.this).load(userInfo.getAvatar()).into(civAvatar);
                                civAvatar.setVisibility(View.VISIBLE);
                                tvName.setVisibility(View.VISIBLE);
                                tvName.setText(userInfo.getName());
                                btnLogin.setVisibility(View.GONE);
                                btnLogout.setVisibility(View.VISIBLE);
                                mRoot.child("Home").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(userInfo.getId())) {
                                            userInfo.setCoint(0);
                                            mRoot.child("Home").child(userInfo.getId()).setValue(userInfo);
                                            tvCoint.setText("0");
                                        } else {
                                            mRoot.child("Home").child(userInfo.getId()).setValue(userInfo);
                                            updateCoin(userInfo);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                ).executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == RESULT_OK) {
                startService(new Intent(MainActivity.this, FloatingViewService.class));
                finish();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkLogin() {
        if (AccessToken.getCurrentAccessToken() != null) {
            Glide.with(MainActivity.this).load(utils.getUserInfo().getAvatar()).into(civAvatar);
            tvName.setText(utils.getUserInfo().getName());
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
            listenerInvite();
            rlNoti.setVisibility(View.VISIBLE);
            listenNotification();
            updateCoin(utils.getUserInfo());
            listenerResult();
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

    private void listenerInvite() {
        listenerAccept = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String isAccept = dataSnapshot.getValue(String.class);
                    if (isAccept.equals("ok")) {
                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Đối thủ chấp nhận lời mời của bạn!!!")
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mRoot.child("Home").child(utils.getIdInvite()).child("Invite").child(utils.getKeyInvite())
                                                .removeValue();
                                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                                        intent.putExtra(Common.INFO_RIVAL, utils.getIdInvite());
//                                        utils.saveIdInvite("");
                                        utils.saveKeyInvite("");
                                        startActivity(intent);
                                    }
                                }).show();
                    } else if (isAccept.equals("reject")) {
                        mRoot.child("Home").child(utils.getIdInvite()).child("Invite").child(utils.getKeyInvite())
                                .removeValue();
                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Đối thủ đã từ chối lời mời")
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        utils.saveIdInvite("");
                                        utils.saveKeyInvite("");
                                    }
                                }).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Log.d("listenerInvite", "listenerInvite: " + utils.getKeyInvite() + "\n" + utils.getIdInvite());
        if (utils.getIdInvite() != null && utils.getKeyInvite() != null
                && !utils.getIdInvite().equals("") && !utils.getKeyInvite().equals("")) {
            mRoot.child("Home").child(utils.getIdInvite()).child("Invite").child(utils.getKeyInvite())
                    .child("result").addValueEventListener(listenerAccept);
        }
    }

    private void listenNotification() {
        listenerNoti = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long numberNoti = dataSnapshot.getChildrenCount();
                if (numberNoti == 0) {
                    tvNoti.setVisibility(View.GONE);
                } else {
                    tvNoti.setVisibility(View.VISIBLE);
                    tvNoti.setText(numberNoti + "");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRoot.child("Home").child(utils.getUserInfo().getId()).child("Invite").addValueEventListener(listenerNoti);
    }

    private void updateCoin(UserInfo userInfo) {
        mRoot.child("Home").child(userInfo.getId()).child("coint").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int coint = dataSnapshot.getValue(Integer.class);
                tvCoint.setText(coint + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void listenerResult() {

        if (utils.getKeyPlay() != null) {
            mRoot.child("Play").child(utils.getKeyPlay()).child(utils.getUserInfo().getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    myPoint = dataSnapshot.getValue(Integer.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mRoot.child("Play").child(utils.getKeyPlay()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(utils.getIdInvite())) {
                        mRoot.child("Play").child(utils.getKeyPlay()).child(utils.getIdInvite()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                rivalPoint = dataSnapshot.getValue(Integer.class);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            String result;
            if (myPoint > rivalPoint) {
                result = "Thắng";
                utils.getUserInfo().setCoint(utils.getUserInfo().getCoint() + 1);
                mRoot.child("Home").child(utils.getUserInfo().getId()).child("coint").setValue(utils.getUserInfo().getCoint() + 1);
            } else if (myPoint == rivalPoint) {
                result = "Hòa";
            } else {
                result = "Thua";
                utils.getUserInfo().setCoint(utils.getUserInfo().getCoint() - 1);
                mRoot.child("Home").child(utils.getUserInfo().getId()).child("coint").setValue(utils.getUserInfo().getCoint() - 1);
            }
            deleteRoom();
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(result)
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            mRoot.child("Play").child(utils.getKeyPlay()).child("out").setValue(1);
                            //1: out
                            utils.saveKeyPlay(null);
                        }
                    })
                    .setCancelable(false)
                    .show();

        }
    }

    private void deleteRoom() {
        mRoot.child("Play").child(utils.getKeyPlay()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("out")) {
                    mRoot.child("Play").child(utils.getKeyPlay()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

