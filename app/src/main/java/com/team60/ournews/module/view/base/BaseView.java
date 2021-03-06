package com.team60.ournews.module.view.base;

import io.reactivex.disposables.Disposable;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public interface BaseView {
    void showSnackBar(String message);

    void addSubscription(Disposable disposable);
}
