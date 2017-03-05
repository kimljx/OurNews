package com.team60.ournews.widget;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misutesu on 2016/12/27 0027.
 */

public class BrowseView extends LinearLayout {

    private final int TWO_CLICK_TIME_INTERVAL = 4000;

    private Context context;
    private ImageView mTypeImg;
    private TextView mTypeText;
    private LinearLayout mMoreLayout;
    private LinearLayout mRefreshLayout;
    private TextView mRefreshText;
    private ImageView mRefreshImg;

    private List<New> news;
    private List<CardView> mCardViews;
    private List<SimpleDraweeView> mCoverImages;
    private List<TextView> mTitleTexts;

    private AlphaAnimation inAnimation = new AlphaAnimation(0, 1);
    private AlphaAnimation outAnimation = new AlphaAnimation(1, 0);
    private RotateAnimation rotateAnimation = new RotateAnimation(0, 3600
            , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    private boolean isRefreshAll = false;
    private boolean isRefresh = false;
    private long lastClickTime = 0;

    private OnActionListener onActionListener;

    public interface OnActionListener {
        void onBtnClick();

        void onRefreshClick();

        void onTwoClickTooClose(String message);

        void onNewClick(New n, View view);
    }

    public BrowseView(Context context) {
        super(context);
        init(context);
    }

    public BrowseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BrowseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setTypeImg(int resId) {
        mTypeImg.setImageResource(resId);
    }

    public void setTypeText(String text) {
        mTypeText.setText(text);
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public void startRefresh() {
        isRefreshAll = true;
    }

    public void startLocalRefresh() {
        isRefresh = true;
    }

    public void endRefresh() {
        isRefreshAll = false;
        if (isRefresh) {
            rotateAnimation.cancel();
            isRefresh = false;
            mRefreshText.setText(MyApplication.getContext().getString(R.string.change_a_lot));
        }
    }

    public void setData(List<New> news) {
        if (this.news == null)
            this.news = new ArrayList<>();
        this.news.clear();
        this.news.addAll(news);
        showData();
    }

    private void init(Context context) {
        setOrientation(VERTICAL);

        this.context = context;

        inAnimation.setDuration(200);
        outAnimation.setDuration(200);
        rotateAnimation.setDuration(5000);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setInterpolator(new LinearInterpolator());

        View view = LayoutInflater.from(context).inflate(R.layout.layout_browse_view, this, true);

        mCardViews = new ArrayList<>();
        mCoverImages = new ArrayList<>();
        mTitleTexts = new ArrayList<>();

        mTypeImg = (ImageView) view.findViewById(R.id.browse_view_type_img);
        mTypeText = (TextView) view.findViewById(R.id.browse_view_type_text);
        mMoreLayout = (LinearLayout) view.findViewById(R.id.browse_view_see_more_layout);
        mRefreshLayout = (LinearLayout) view.findViewById(R.id.browse_view_refresh_layout);
        mRefreshText = (TextView) view.findViewById(R.id.browse_view_refresh_text);
        mRefreshImg = (ImageView) view.findViewById(R.id.browse_view_refresh_img);

        mCardViews.add((CardView) view.findViewById(R.id.item_home_card_view_1));
        mCardViews.add((CardView) view.findViewById(R.id.item_home_card_view_2));
        mCardViews.add((CardView) view.findViewById(R.id.item_home_card_view_3));
        mCardViews.add((CardView) view.findViewById(R.id.item_home_card_view_4));

        mCoverImages.add((SimpleDraweeView) view.findViewById(R.id.item_home_cover_img_1));
        mCoverImages.add((SimpleDraweeView) view.findViewById(R.id.item_home_cover_img_2));
        mCoverImages.add((SimpleDraweeView) view.findViewById(R.id.item_home_cover_img_3));
        mCoverImages.add((SimpleDraweeView) view.findViewById(R.id.item_home_cover_img_4));

        mTitleTexts.add((TextView) view.findViewById(R.id.item_home_title_text_1));
        mTitleTexts.add((TextView) view.findViewById(R.id.item_home_title_text_2));
        mTitleTexts.add((TextView) view.findViewById(R.id.item_home_title_text_3));
        mTitleTexts.add((TextView) view.findViewById(R.id.item_home_title_text_4));

        mMoreLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionListener != null) {
                    onActionListener.onBtnClick();
                }
            }
        });

        mRefreshLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionListener != null && BrowseView.this.news != null && !isRefresh && !isRefreshAll) {
                    if (System.currentTimeMillis() - lastClickTime < TWO_CLICK_TIME_INTERVAL) {
                        onActionListener.onTwoClickTooClose(MyApplication.getContext().getString(R.string.click_two_too_close));
                    } else {
                        lastClickTime = System.currentTimeMillis();
                        onActionListener.onRefreshClick();
                        mRefreshImg.startAnimation(rotateAnimation);
                        mRefreshText.setText(MyApplication.getContext().getString(R.string.is_refreshing));
                    }
                }
            }
        });
    }

    private void showData() {
        for (int i = 0; i < 4; i++) {
            final int finalI = i;
            final New n = BrowseView.this.news.get(i);
            FrescoLoader.load(MyUtil.getPhotoUrl(n.getCover())).into(mCoverImages.get(i));
            mTitleTexts.get(i).setText(n.getTitle());
            mCardViews.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isRefresh && !isRefreshAll) {
                        if (onActionListener != null) {
                            onActionListener.onNewClick(n, mCoverImages.get(finalI));
                        }
                    }
                }
            });
            startAnimation(mCardViews.get(i));
        }
    }

    private void startAnimation(View view) {
        view.startAnimation(outAnimation);
        view.startAnimation(inAnimation);
    }
}
