package com.yasic.bubbleview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Yasic on 2016/6/1.
 */
public class BubbleView extends RelativeLayout {
    private List<Drawable> drawableList = new ArrayList<>();

    private int viewWidth = dp2pix(16), viewHeight = dp2pix(16);

    private int maxHeartNum = 8;
    private int minHeartNum = 2;

    private int riseDuration = 4000;

    private int bottomPadding = 200;
    private int originsOffset = 60;

    private float maxScale = 1.0f;
    private float minScale = 1.0f;

    private int innerDelay = 200;

    public BubbleView(Context context) {
        super(context);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BubbleView setDrawableList(List<Drawable> drawableList) {
        this.drawableList = drawableList;
        return this;
    }

    public BubbleView setRiseDuration(int riseDuration) {
        this.riseDuration = riseDuration;
        return this;
    }

    public BubbleView setBottomPadding(int px) {
        this.bottomPadding = px;
        return this;
    }

    public BubbleView setOriginsOffset(int px) {
        this.originsOffset = px;
        return this;
    }

    public BubbleView setScaleAnimation(float maxScale, float minScale) {
        this.maxScale = maxScale;
        this.minScale = minScale;
        return this;
    }

    public BubbleView setAnimationDelay(int delay) {
        this.innerDelay = delay;
        return this;
    }

    public void setMaxHeartNum(int maxHeartNum) {
        this.maxHeartNum = maxHeartNum;
    }

    public void setMinHeartNum(int minHeartNum) {
        this.minHeartNum = minHeartNum;
    }

    public BubbleView setItemViewWH(int viewWidth, int viewHeight) {
        this.viewHeight = viewHeight;
        this.viewWidth = viewWidth;
        return this;
    }

    public BubbleView setGiftBoxImaeg(Drawable drawable, int positionX, int positionY) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawable);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageView.getWidth(), imageView.getHeight());
        this.addView(imageView, layoutParams);
        imageView.setX(positionX);
        imageView.setY(positionY);
        return this;
    }

    public void startAnimation(final int rankWidth, final int rankHeight) {
        //TODO 去除Rx
        //每隔innerDelay的时间就触发一遍bubbleAnimation，重复次数repeat
        int repeat = (int) (Math.random() * (maxHeartNum - minHeartNum + 1)) + minHeartNum;
        new CountDownTimer(innerDelay * repeat, innerDelay) {
            public void onTick(long millisUntilFinished) {
                bubbleAnimation(rankWidth, rankHeight);
            }

            public void onFinish() {
                bubbleAnimation(rankWidth, rankHeight);
            }
        }.start();
//        Observable.timer(innerDelay, TimeUnit.MILLISECONDS)
//                .repeat((int)(Math.random() * (maxHeartNum - minHeartNum + 1)) + minHeartNum)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        bubbleAnimation(rankWidth, rankHeight);
//                    }
//                });
    }

    public void startAnimation(final int rankWidth, final int rankHeight, int count) {
        new CountDownTimer(innerDelay * count, innerDelay) {
            public void onTick(long millisUntilFinished) {
                bubbleAnimation(rankWidth, rankHeight);
            }

            public void onFinish() {
                bubbleAnimation(rankWidth, rankHeight);
            }
        }.start();
//        Observable.timer(innerDelay, TimeUnit.MILLISECONDS)
//                .repeat(count)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        bubbleAnimation(rankWidth, rankHeight);
//                    }
//                });
    }

    @Deprecated
    public void startAnimation(final int rankWidth, final int rankHeight, int delay, int count) {
//        new CountDownTimer(innerDelay * count, innerDelay) {
//            public void onTick(long millisUntilFinished) {
//                bubbleAnimation(rankWidth, rankHeight);
//            }
//
//            public void onFinish() {
//                bubbleAnimation(rankWidth, rankHeight);
//            }
//        }.start();

//        Observable.timer(delay, TimeUnit.MILLISECONDS)
//                .repeat(count)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        bubbleAnimation(rankWidth, rankHeight);
//                    }
//                });
    }

    private void bubbleAnimation(int rankWidth, int rankHeight) {
        rankHeight -= bottomPadding;//自己设定的paddingBottom
        //改变萌发点的位置
        int seed = (int) (Math.random() * 3);
        switch (seed) {
            case 0:
                rankWidth -= originsOffset;
                break;
            case 1:
                rankWidth += originsOffset;
                break;
            case 2:
                rankHeight -= originsOffset;
                break;
        }
        //创建ImageView取出随机的一个图片
        int heartDrawableIndex;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewWidth, viewHeight);
        heartDrawableIndex = (int) (drawableList.size() * Math.random());
        ImageView tempImageView = new ImageView(getContext());
        tempImageView.setImageDrawable(drawableList.get(heartDrawableIndex));
        addView(tempImageView, layoutParams);
        //动画(透明度+放大)
        ObjectAnimator riseAlphaAnimator = ObjectAnimator.ofFloat(tempImageView, "alpha", 1.0f, 0.0f);
        riseAlphaAnimator.setDuration(riseDuration);

        ObjectAnimator riseScaleXAnimator = ObjectAnimator.ofFloat(tempImageView, "scaleX", minScale, maxScale*2);
        riseScaleXAnimator.setDuration(riseDuration);

        ObjectAnimator riseScaleYAnimator = ObjectAnimator.ofFloat(tempImageView, "scaleY", minScale, maxScale*2);
        riseScaleYAnimator.setDuration(riseDuration);
        //贝塞尔曲线的路径
        ValueAnimator valueAnimator = getBesselAnimator(tempImageView, rankWidth, rankHeight);
        //组合播放动画
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(valueAnimator).with(riseAlphaAnimator).with(riseScaleXAnimator).with(riseScaleYAnimator);
        animatorSet.start();
    }
    //四个点从何而得
    private ValueAnimator getBesselAnimator(final ImageView imageView, int rankWidth, int rankHeight) {
        float point0[] = new float[2];
        point0[0] = rankWidth / 2;
        point0[1] = rankHeight;

        float point1[] = new float[2];
        point1[0] = (float) ((rankWidth) * (0.10)) + (float) (Math.random() * (rankWidth) * (0.8));
        point1[1] = (float) (rankHeight - Math.random() * rankHeight * (0.5));

        float point2[] = new float[2];
        point2[0] = (float) (Math.random() * rankWidth);
        point2[1] = (float) (Math.random() * (rankHeight - point1[1]));

        float point3[] = new float[2];
        point3[0] = (float) (Math.random() * rankWidth);
        point3[1] = 0;
        //插值器干涉加速度 估值器干涉路径
        BesselEvaluator besselEvaluator = new BesselEvaluator(point1, point2);//1/2此处传入
        ValueAnimator valueAnimator = ValueAnimator.ofObject(besselEvaluator, point0, point3);//0/3此处传入 为start和end
        valueAnimator.setDuration(riseDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float[] currentPosition = new float[2];
                currentPosition = (float[]) animation.getAnimatedValue();
                imageView.setTranslationX(currentPosition[0]);
                imageView.setTranslationY(currentPosition[1]);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(imageView);
                imageView.setImageDrawable(null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return valueAnimator;
    }
    //实现TypeEvaluator接口
    public class BesselEvaluator implements TypeEvaluator<float[]> {
        private float point1[] = new float[2], point2[] = new float[2];

        public BesselEvaluator(float[] point1, float[] point2) {
            this.point1 = point1;
            this.point2 = point2;
        }

        @Override
        public float[] evaluate(float fraction, float[] point0, float[] point3) {
            float[] currentPosition = new float[2];
            currentPosition[0] = point0[0] * (1 - fraction) * (1 - fraction) * (1 - fraction)//P0(1-t)3
                    + point1[0] * 3 * fraction * (1 - fraction) * (1 - fraction)//3P1*t*(1-t)2
                    + point2[0] * 3 * (1 - fraction) * fraction * fraction//3P2t*t(1-t)
                    + point3[0] * fraction * fraction * fraction;//P3t3
            currentPosition[1] = point0[1] * (1 - fraction) * (1 - fraction) * (1 - fraction)
                    + point1[1] * 3 * fraction * (1 - fraction) * (1 - fraction)
                    + point2[1] * 3 * (1 - fraction) * fraction * fraction
                    + point3[1] * fraction * fraction * fraction;
            return currentPosition;
        }
    }

    private int dp2pix(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        int pix = (int) (dp * scale + 0.5f);
        return pix;
    }

    private int pix2dp(int pix) {
        float scale = getResources().getDisplayMetrics().density;
        int dp = (int) (pix / scale + 0.5f);
        return dp;
    }
}
