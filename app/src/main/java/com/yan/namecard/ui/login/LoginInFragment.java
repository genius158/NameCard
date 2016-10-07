package com.yan.namecard.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.widget.LoginButton;
import com.yan.namecard.R;
import com.yan.namecard.ui.base.BaseFragment;
import com.yan.namecard.ui.cardedit.CardEditFragment;
import com.yan.namecard.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yan on 2016/10/5.
 */
public class LoginInFragment extends BaseFragment implements LoginContract.View {
    private LoginContract.Presenter loginPresenter;

    @BindView(R.id.btn_login_weibo)
    LoginButton btnLoginWeibo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        new LogInPresenter(getActivity(), this).attachView();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.loginPresenter = presenter;
    }

    @Override
    public void setWeiboAuthInfo(AuthInfo mAuthInfo, WeiboAuthListener listener) {
        btnLoginWeibo.setWeiboAuthInfo(mAuthInfo, listener);
    }

    @OnClick({R.id.btn_login_tencent, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_tencent:
                loginPresenter.loginTencent();

                break;
            case R.id.btn_login:
                ((MainActivity) getActivity()).replaceFragment(CardEditFragment.instance(CardEditFragment.class));
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        btnLoginWeibo.onActivityResult(requestCode, resultCode, data);
        loginPresenter.onActivityResult(requestCode, resultCode, data);
    }
}
