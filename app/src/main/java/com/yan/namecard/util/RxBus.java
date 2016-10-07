package com.yan.namecard.util;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by yan on 2016/9/18.
 */
public final class RxBus {
    private volatile static RxBus rxBus;
    private Subject BUS;

    private RxBus() {
        BUS = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getInstance() {
        if (rxBus == null)
            synchronized (RxBus.class) {
                if (rxBus == null) rxBus = new RxBus();
            }
        return rxBus;
    }

    /**
     * post event
     *
     * @param event event
     */
    public synchronized void post(Object event) {
        BUS.onNext(event);
    }

    protected <T> Observable<T> toObservable(Class<T> eventType) {
        return BUS.ofType(eventType);
    }

}
