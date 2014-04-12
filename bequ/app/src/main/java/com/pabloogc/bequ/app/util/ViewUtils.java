package com.pabloogc.bequ.app.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by Pablo Orgaz - 3/9/14 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class ViewUtils {

    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void verticalSmoothScroll(ScrollView scrollView, View view) {
        if (Build.VERSION.SDK_INT > 10) {
            ObjectAnimator animator = ObjectAnimator.ofInt(scrollView, "scrollY", scrollView.getScrollY(), view.getTop());
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(333);
            animator.start();
        } else
            scrollView.smoothScrollTo(0, view.getTop());
    }

    public static void animateVisible(View view, int durationMillis) {
        animateVisible(view, durationMillis, 0);
    }

    public static void animateVisible(final View view, int durationMillis, int delay) {
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1);
        alphaAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
        alphaAnim.setInterpolator(new DecelerateInterpolator());
        alphaAnim.setDuration(durationMillis);
        alphaAnim.setStartDelay(delay);
        alphaAnim.start();
    }

    public static Bitmap createBitmap(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    private static Animator configureSlideAnimator(Animator animator) {
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }

    public static Animator slideInRight(View target) {
        return configureSlideAnimator(ObjectAnimator.ofFloat(target, View.TRANSLATION_X, target.getWidth(), 0));
    }

    public static Animator slideInLeft(View target) {
        return configureSlideAnimator(ObjectAnimator.ofFloat(target, View.TRANSLATION_X, -target.getWidth(), 0));
    }

    public static Animator slideOutLeft(View target) {
        return configureSlideAnimator(ObjectAnimator.ofFloat(target, View.TRANSLATION_X, 0, -target.getWidth()));
    }

    public static Animator slideOutRight(View target) {
        return configureSlideAnimator(ObjectAnimator.ofFloat(target, View.TRANSLATION_X, 0, target.getWidth()));
    }


}
