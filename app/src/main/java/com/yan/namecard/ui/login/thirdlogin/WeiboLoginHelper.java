package com.yan.namecard.ui.login.thirdlogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by yan on 2016/10/5.
 */
public class WeiboLoginHelper {
    private static AccessTokenKeeper accessTokenKeeper;

    private WeiboLoginHelper() {
        accessTokenKeeper = new AccessTokenKeeper();
    }

    public static AccessTokenKeeper getAccessTokenKeeper() {
        if (accessTokenKeeper == null)
            synchronized (WeiboLoginHelper.class) {
                if (accessTokenKeeper == null)
                    accessTokenKeeper = new AccessTokenKeeper();
            }
        return accessTokenKeeper;
    }

    /**
     * Created by yan on 2016/10/5.
     */
    public static class AccessTokenKeeper {
        private static final String PREFERENCES_NAME = "com_weibo_sdk_android";

        private static final String KEY_UID = "uid";
        private static final String KEY_ACCESS_TOKEN = "access_token";
        private static final String KEY_EXPIRES_IN = "expires_in";
        private static final String KEY_REFRESH_TOKEN = "refresh_token";

        /**
         * 保存 Token 对象到 SharedPreferences。
         *
         * @param context 应用程序上下文环境
         * @param token   Token 对象
         */
        public void writeAccessToken(Context context, Oauth2AccessToken token) {
            if (null == context || null == token) {
                return;
            }

            SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(KEY_UID, token.getUid());
            editor.putString(KEY_ACCESS_TOKEN, token.getToken());
            editor.putString(KEY_REFRESH_TOKEN, token.getRefreshToken());
            editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
            editor.commit();
        }

        /**
         * 从 SharedPreferences 读取 Token 信息。
         *
         * @param context 应用程序上下文环境
         * @return 返回 Token 对象
         */
        public Oauth2AccessToken readAccessToken(Context context) {
            if (null == context) {
                return null;
            }

            Oauth2AccessToken token = new Oauth2AccessToken();
            SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
            token.setUid(pref.getString(KEY_UID, ""));
            token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
            token.setRefreshToken(pref.getString(KEY_REFRESH_TOKEN, ""));
            token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));

            return token;
        }

        /**
         * 清空 SharedPreferences 中 Token信息。
         *
         * @param context 应用程序上下文环境
         */
        public void clear(Context context) {
            if (null == context) {
                return;
            }

            SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
        }
    }


    public static class WeiboAuthListenerAdapter implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle bundle) {

        }

        @Override
        public void onWeiboException(WeiboException e) {

        }

        @Override
        public void onCancel() {

        }
    }
}