package com.example.admin.btloop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.admin.btloop.R;
import com.example.admin.btloop.activity.GameActivity;
import com.example.admin.btloop.activity.MainActivity;
import com.example.admin.btloop.adapter.NotiAdapter;
import com.example.admin.btloop.model.Noti;
import com.example.admin.btloop.utils.Common;
import com.example.admin.btloop.utils.SharedPreferencesUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 11/13/2017.
 */

public class NotificationDialog extends Dialog implements View.OnClickListener {
    private RecyclerView rvNoti;
    private NotiAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Noti> list = new ArrayList<>();
    private Noti noti;
    private Button btnReject;
    private Button btnOk;
    private ImageView imgClose;
    private DatabaseReference mRoot;
    private SharedPreferencesUtils utils;
    private ValueEventListener listenerNotification;

    public NotificationDialog(@NonNull Context context, @StyleRes int themeResId, List<Noti> list) {
        super(context, themeResId);
        this.list = list;
    }

    public NotificationDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notification);
        utils = new SharedPreferencesUtils(getContext());
        mRoot = FirebaseDatabase.getInstance().getReference();
        rvNoti = (RecyclerView) findViewById(R.id.rv_noti);
        btnOk = (Button) findViewById(R.id.btn_accept);
        btnReject = (Button) findViewById(R.id.btn_reject);
        imgClose = (ImageView) findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getContext());
        rvNoti.setLayoutManager(layoutManager);
        listenerNotification = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Noti noti = snapshot.getValue(Noti.class);
                    list.add(noti);
                }
                adapter = new NotiAdapter(getContext(), list, new NotiAdapter.click() {
                    @Override
                    public void click(int pos) {
                        noti = list.get(pos);
                    }
                });
                adapter.notifyDataSetChanged();
                rvNoti.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRoot.child("Home").child(utils.getUserInfo().getId()).child("Invite").addValueEventListener(listenerNotification);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_accept:
                acceptInvite(noti);
                break;
            case R.id.btn_reject:
                rejectInvite(noti);
                break;
            case R.id.img_close:
                dismiss();
                break;
        }
    }

    private void rejectInvite(Noti noti) {
        mRoot.child("Home").child(utils.getUserInfo().getId()).child("Invite")
                .child(noti.getKey()).child("result").setValue("reject");
    }

    private void acceptInvite(Noti noti) {
        mRoot.child("Home").child(utils.getUserInfo().getId()).child("Invite")
                .child(noti.getKey()).child("result").setValue("ok");
        Intent intent = new Intent(getContext(), GameActivity.class);
        intent.putExtra(Common.INFO_RIVAL, noti);
        getContext().startActivity(intent);
    }
}
