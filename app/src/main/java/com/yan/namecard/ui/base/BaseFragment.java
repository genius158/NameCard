package com.yan.namecard.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseFragment extends Fragment {
    private CompositeSubscription mSubscriptions;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscriptions != null) {
            mSubscriptions.clear();
        }
    }

    public static <T extends BaseFragment> T instance(Class aClass, Bundle args) {
        BaseFragment fragment = null;
        try {
            fragment = (BaseFragment) aClass.newInstance();
            fragment.setArguments(args);
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) fragment;
    }

    public static <T extends BaseFragment> T instance(Class aClass) {
        return instance(aClass, new Bundle());
    }

    public void setArguments(Bundle args) {
    }


    protected void addSubscription(Subscription subscription) {
        if (subscription == null) return;
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
        mSubscriptions.add(subscription);
    }

}
