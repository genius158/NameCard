package com.yan.namecard.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.yan.namecard.common.Config;
import com.yan.namecard.ui.login.thirdlogin.TencentLogin;
import com.yan.namecard.ui.login.thirdlogin.WeiboLoginHelper;


/**
 * Created by yan on 2016/10/5.
 */
public class LogInPresenter implements LoginContract.Presenter {
    private LoginContract.View loginView;
    private Activity context;
    private AuthInfo mAuthInfo;

    public LogInPresenter(Activity context, LoginContract.View loginView) {
        this.context = context;
        this.loginView = loginView;
        this.loginView.setPresenter(this);
    }

    /**
     * loginTencent
     */
    @Override
    public void loginTencent() {
        TencentLogin.get(context).login(new TencentLogin.IUiListenerAdapter() {
            @Override
            public void onComplete(Object o) {
                Log.i("tag", o + "");
            }
        });
    }

    void initLogInWeibo() {
        mAuthInfo = new AuthInfo(context, Config.WEIBO_APP_KEY, Config.WEIBO_REDIRECT_URL, Config.WEIBO_SCOPE);

        loginView.setWeiboAuthInfo(mAuthInfo, new WeiboLoginHelper.WeiboAuthListenerAdapter() {
            @Override
            public void onComplete(Bundle values) {
                Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
                if (accessToken != null && accessToken.isSessionValid()) {
                    WeiboLoginHelper.getAccessTokenKeeper().writeAccessToken(context.getApplicationContext(), accessToken);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        TencentLogin.get(context).onActivityResultData(requestCode, resultCode, data);
    }

    @Override
    public void attachView() {
        initLogInWeibo();
    }

    @Override
    public void detachView() {
        loginView = null;
        context = null;
        mAuthInfo = null;
    }
}
