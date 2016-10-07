package com.yan.namecard.ui.login.thirdlogin;

import android.app.Activity;
import android.content.Intent;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yan.namecard.common.Config;
import com.yan.namecard.util.ToastHelper;

import org.json.JSONObject;

/**
 * Created by yan on 2016/10/5.
 */
public class TencentLogin {
    private Tencent mTencent;
    private Activity activity;
    private IUiListener iUiListener;

    public static volatile TencentLogin tencentLogin;

    public static TencentLogin get(Activity activity) {
        if (tencentLogin == null)
            synchronized (TencentLogin.class) {
                if (tencentLogin == null)
                    tencentLogin = new TencentLogin(activity);
            }
        return tencentLogin;
    }

    private TencentLogin(Activity activity) {
        this.activity = activity;
        mTencent = Tencent.createInstance(Config.TENCENT_APP_ID, this.activity.getBaseContext());
    }

    public static class IUiListenerAdapter implements IUiListener {

        @Override
        public void onComplete(Object o) {

        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    private IUiListenerAdapter baseUiListener = new IUiListenerAdapter() {
        @Override
        public void onComplete(Object obj) {
            try {
                JSONObject jo = (JSONObject) obj;
                int ret = jo.getInt("ret");
                System.out.println("json=" + String.valueOf(jo));

                if (ret == 0) {
                    ToastHelper.getInstance().showToast("登录成功");

                    String openID = jo.getString("openid");
                    String accessToken = jo.getString("access_token");
                    String expires = jo.getString("expires_in");
                    mTencent.setOpenId(openID);
                    mTencent.setAccessToken(accessToken, expires);

                    UserInfo mInfo = new UserInfo(TencentLogin.this.activity, mTencent.getQQToken());
                    mInfo.getUserInfo(TencentLogin.this.iUiListener);
                }
            } catch (Exception e) {
            }
        }
    };

    public void login(IUiListener iUiListener) {
        this.iUiListener = iUiListener;
        if (!mTencent.isSessionValid()) {
            mTencent.login(this.activity, "all", baseUiListener);
        }
    }

    public void logout() {
        mTencent.logout(activity.getBaseContext());
        activity = null;
    }

    public void onActivityResultData(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, baseUiListener);
    }

}
