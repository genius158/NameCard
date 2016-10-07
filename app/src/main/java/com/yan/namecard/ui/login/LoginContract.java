package com.yan.namecard.ui.login;

import android.content.Intent;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.yan.namecard.ui.base.IBasePresenter;
import com.yan.namecard.ui.base.IBaseView;

/**
 * Created by yan on 2016/10/5.
 */
public interface LoginContract {

    interface View extends IBaseView<Presenter> {
        void setWeiboAuthInfo(AuthInfo mAuthInfo, WeiboAuthListener listener);


    }

    interface Presenter extends IBasePresenter {

        void loginTencent();
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
