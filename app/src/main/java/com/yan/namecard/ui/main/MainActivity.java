package com.yan.namecard.ui.main;

import android.content.Intent;
import android.os.Bundle;
import com.yan.namecard.R;
import com.yan.namecard.ui.base.BaseActivity;
import com.yan.namecard.ui.base.BaseFragment;
import com.yan.namecard.ui.login.LoginInFragment;

import butterknife.ButterKnife;

/**
 * Created by yan on 2016/10/5.
 */
public class MainActivity extends BaseActivity {

    private BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        currentFragment = LoginInFragment.instance(LoginInFragment.class);
        addFragment(currentFragment);
    }


    public void addFragment(BaseFragment fragment) {
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, currentFragment).commit();
    }


    public void replaceFragment(BaseFragment fragment) {
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, currentFragment).commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (currentFragment instanceof LoginInFragment)
            currentFragment.onActivityResult(requestCode, resultCode, data);
    }

}



