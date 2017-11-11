package com.example.admin.btloop.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.admin.btloop.R;
import com.example.admin.btloop.adapter.FriendsAdapter;
import com.example.admin.btloop.model.Friends;
import com.example.admin.btloop.model.ListFriends;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {
    private RecyclerView rvFriends;
    private Button btnInvite;
    private FriendsAdapter adapter;
    private List<Friends> list = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        iniUI();
    }

    protected void iniUI() {
        rvFriends = (RecyclerView) findViewById(R.id.rv_friends);
        btnInvite = (Button) findViewById(R.id.btn_invite);
        layoutManager = new LinearLayoutManager(this);
        rvFriends.setLayoutManager(layoutManager);
        getData();
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
                        adapter = new FriendsAdapter(FriendsActivity.this, list);
                        adapter.notifyDataSetChanged();
                        rvFriends.setAdapter(adapter);
                    }
                }
        ).executeAsync();
    }
}
