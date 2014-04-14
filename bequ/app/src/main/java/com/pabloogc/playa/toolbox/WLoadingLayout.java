package com.pabloogc.playa.toolbox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pabloogc.bequ.app.R;
import com.pabloogc.bequ.app.util.ViewUtils;
import com.pabloogc.playa.handlers.LoadingHandler;


/**
 * Created by pablo on 9/29/13.
 * <p/>
 * Custom layout created to work with {@link com.pabloogc.playa.models.PlayaRequest} that automatically
 * creates a retry button when an error happens and switches between content and loading view automatically.
 */
public class WLoadingLayout extends RelativeLayout implements LoadingHandler {

    private static final int ANIMATION_TIME = 333;

    private LinearLayout retryLayout;
    private TextView errorTextView;
    private TextView retryButton;
    private ProgressBar mProgressBar;
    private OnRetryClickListener onRetryClickListener;

    public WLoadingLayout(Context context) {
        super(context);
    }

    public WLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WLoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    public void setOnRetryClickListener(OnRetryClickListener onRetryClickListener) {
        this.onRetryClickListener = onRetryClickListener;
    }

    private void init(Context context, AttributeSet attrs) {
        retryLayout = new LinearLayout(context);
        retryLayout.setOrientation(LinearLayout.VERTICAL);
        retryLayout.setGravity(Gravity.CENTER);
        retryLayout.setVisibility(INVISIBLE);

        mProgressBar = new ProgressBar(context);
        mProgressBar.setVisibility(GONE);

        errorTextView = new TextView(context);
        errorTextView.setText("Error");
        errorTextView.setGravity(Gravity.CENTER);
        errorTextView.setPadding(0, 0, 0, 16);

        retryButton = new Button(context);
        retryButton.setText("Reintentar");

        retryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRetryClickListener != null)
                    onRetryClickListener.onRetry(view);
            }
        });

        LayoutParams progressLayout = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        progressLayout.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);

        LayoutParams retryLayoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        retryLayoutParams.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
        int margins = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        retryLayoutParams.setMargins(margins, 0, margins, 0);

        retryLayout.addView(errorTextView);
        retryLayout.addView(retryButton);

        LinearLayout.LayoutParams retryButtonLayout =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        retryButton.setLayoutParams(retryButtonLayout);

        addView(mProgressBar, getChildCount(), progressLayout);
        addView(retryLayout, getChildCount(), retryLayoutParams);
    }

    @Override public void showLoading(String message) {
        hideContent();
        retryLayout.setVisibility(GONE);
        ViewUtils.animateVisible(mProgressBar, 100);
    }

    @Override
    public void hideLoading(String message, boolean success) {
        mProgressBar.setVisibility(GONE);
        //Something went wrong, show the retry layout.
        if (success) {
            retryLayout.setVisibility(GONE);
            showContent();
        } else {
            ViewUtils.animateVisible(retryLayout, ANIMATION_TIME);
            errorTextView.setText(message != null ? message : "Algo ha ido mal :(");
        }
    }

    public void hideContent() {
        for (int i = 2; i < getChildCount(); i++)
            getChildAt(i).setVisibility(INVISIBLE);
    }

    public void showContent() {
        for (int i = 2; i < getChildCount(); i++)
            ViewUtils.animateVisible(getChildAt(i), ANIMATION_TIME);
    }

    public interface OnRetryClickListener {
        public void onRetry(View view);
    }
}
