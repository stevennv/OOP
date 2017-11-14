package com.example.admin.btloop.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.btloop.R;
import com.example.admin.btloop.adapter.FriendsAdapter;
import com.example.admin.btloop.model.Friends;
import com.example.admin.btloop.model.ListFriends;
import com.example.admin.btloop.utils.SharedPreferencesUtils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvFriends;
    private Button btnInvite;
    private FriendsAdapter adapter;
    private List<Friends> list = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private TextView tvToolBar;
    private Friends friendsInfo;
    private DatabaseReference mRoot;
    private SharedPreferencesUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        iniUI();
    }

    protected void iniUI() {
        utils = new SharedPreferencesUtils(this);
        mRoot = FirebaseDatabase.getInstance().getReference();
        rvFriends = (RecyclerView) findViewById(R.id.rv_friends);
        btnInvite = (Button) findViewById(R.id.btn_invite);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvToolBar = (TextView) findViewById(R.id.tv_title_toolbar_2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvToolBar.setText(getString(R.string.friends));
        layoutManager = new LinearLayoutManager(this);
        rvFriends.setLayoutManager(layoutManager);
        getData();
        btnInvite.setOnClickListener(this);
    }

    private void getData() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        String json = response.getRawResponse();
                        Gson gson = new Gson();
                        ListFriends friends = gson.fromJson(json, ListFriends.class);
                        list = friends.getData();
                        adapter = new FriendsAdapter(FriendsActivity.this, list, new FriendsAdapter.onClick() {
                            @Override
                            public void click(int pos) {
                                friendsInfo = list.get(pos);
                            }
                        });
                        adapter.notifyDataSetChanged();
                        rvFriends.setAdapter(adapter);
                    }
                }
        ).executeAsync();
    }

    @Override
    public void onClick(View view) {
        if (friendsInfo == null) {
            Toast.makeText(this, getString(R.string.pls_select_anyone), Toast.LENGTH_SHORT).show();
        } else {
            inviteToRival(friendsInfo);
        }
    }

    private void inviteToRival(Friends friends) {
        String key = mRoot.child("posts").push().getKey();
        mRoot.child("Home").child(friends.getId()).child("Invite").child(key).setValue(utils.getUserInfo());
        mRoot.child("Home").child(friends.getId()).child("Invite").child(key).child("key").setValue(key);
        mRoot.child("Home").child(friends.getId()).child("Invite").child(key).child("result").setValue("waiting");
        // 1 :waiting...
        // 2 :reject
        // 3 :ok
        utils.saveKeyInvite(key);
        utils.saveIdInvite(friends.getId());
        finish();
    }
}
