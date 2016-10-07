package com.yan.namecard.util;

import android.content.Context;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by yan on 2016/10/5.
 */
public class ToastHelper {
    public static volatile ToastHelper toastHelper;

    private WeakReference<Context> contextWeakReference;
    private Toast toast;

    private ToastHelper() {
    }

    public void setContext(Context context) {
        if (contextWeakReference == null && context != null) {
            contextWeakReference = new WeakReference<>(context);
            if (contextWeakReference.get() != null)
                toast = Toast.makeText(contextWeakReference.get(), "", Toast.LENGTH_SHORT);
        }
    }

    public static ToastHelper getInstance() {
        if (toastHelper == null)
            synchronized (ToastHelper.class) {
                if (toastHelper == null)
                    toastHelper = new ToastHelper();
            }
        return toastHelper;
    }

    public void showToast(String str) {
        toast.setText(str);
        toast.show();
    }

}
