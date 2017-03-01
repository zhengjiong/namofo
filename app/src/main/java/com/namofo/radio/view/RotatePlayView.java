package com.namofo.radio.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.namofo.radio.R;
import com.namofo.radio.util.Utils;

/**
 * Title: RotatePlayView
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/2/9  21:31
 *
 * @author 郑炯
 * @version 1.0
 */
public class RotatePlayView extends RelativeLayout{

    private ImageView mImageView;

    private ObjectAnimator mObjectAnimator;
    private float mLastRatation;

    public RotatePlayView(Context context) {
        this(context, null);
    }

    public RotatePlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotatePlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mImageView = new ImageView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Utils.dip2px(getContext(), 230), Utils.dip2px(getContext(), 230));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mImageView.setLayoutParams(params);
        addView(mImageView);

        mObjectAnimator = ObjectAnimator.ofFloat(mImageView, "rotation", 0, 360);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
        mObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimator.setRepeatCount(-1);
        mObjectAnimator.setDuration(3000);
        mObjectAnimator.addUpdateListener(animation -> {
            mLastRatation = Float.parseFloat(animation.getAnimatedValue().toString());
        });
    }

    public void setImageView(int resId) {
        Glide.with(getContext()).load(resId).asBitmap().centerCrop().into(new BitmapImageViewTarget(mImageView){
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mImageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    public void start(){
        if (!mObjectAnimator.isRunning()) {
            mObjectAnimator.start();
        }
    }

    public void stop(){
        mObjectAnimator.cancel();
        mObjectAnimator.setFloatValues();
        mObjectAnimator.setFloatValues(mLastRatation, mLastRatation + 360);
    }
}
