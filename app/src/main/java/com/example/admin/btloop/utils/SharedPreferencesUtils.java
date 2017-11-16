package com.example.admin.btloop.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.admin.btloop.dialog.UserInfo;
import com.google.gson.Gson;

/**
 * Created by Admin on 11/10/2017.
 */

public class SharedPreferencesUtils {
    private static SharedPreferencesUtils mIntent = null;
    private static final String SHARE_NAME = "SMALL";
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public SharedPreferencesUtils(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        mIntent = this;
        gson = new Gson();
    }

    public static SharedPreferencesUtils getIntent(Context context) {
        if (mIntent == null)
            mIntent = new SharedPreferencesUtils(context);
        return mIntent;
    }

    public void saveUserInfo(UserInfo userInfo) {
        String json = gson.toJson(userInfo);
        editor.putString(Common.USER_INFO, json);
        editor.commit();
    }

    public UserInfo getUserInfo() {
        String json = preferences.getString(Common.USER_INFO, "");
        UserInfo userInfo = gson.fromJson(json, UserInfo.class);
        return userInfo;
    }

    public void saveKeyInvite(String key) {
        editor.putString(Common.KEY_INVITE, key);
        editor.commit();
    }

    public String getKeyInvite() {
        String key = preferences.getString(Common.KEY_INVITE, "");
        return key;
    }

    public void saveIdInvite(String id) {
        editor.putString(Common.ID_INVITE, id);
        editor.commit();
    }

    public String getIdInvite() {
        String id = preferences.getString(Common.ID_INVITE, "");
        return id;
    }

    public void saveKeyPlay(String id) {
        editor.putString(Common.KEY_PLAY, id);
        editor.commit();
    }

    public String getKeyPlay() {
        String id = preferences.getString(Common.KEY_PLAY, null);
        return id;
    }
}
