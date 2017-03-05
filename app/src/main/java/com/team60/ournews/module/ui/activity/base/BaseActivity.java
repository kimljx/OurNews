package com.team60.ournews.module.ui.activity.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.team60.ournews.R;
import com.team60.ournews.module.bean.User;
import com.team60.ournews.util.ThemeUtil;
import com.team60.ournews.util.UiUtil;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public User user = User.newInstance();

    private CompositeSubscription mSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UiUtil.initialize(this);

        if (ThemeUtil.isFirstActivity) {
            ThemeUtil.isFirstActivity = false;
        } else {
            if (ThemeUtil.isNightMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                setTheme(R.style.NightTheme);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                int styleId = ThemeUtil.getStyle();
                if (styleId != -1)
                    setTheme(styleId);
            }
        }
        hideStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && mSubscription.hasSubscriptions())
            mSubscription.clear();
    }

    public void addSubscription(@NonNull Subscription subscription) {
        if (mSubscription == null)
            mSubscription = new CompositeSubscription();
        mSubscription.add(subscription);
    }

    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(ThemeUtil.getColor(getTheme(), R.attr.colorPrimary));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    public abstract void init(Bundle savedInstanceState);

    public abstract void setListener();
}
