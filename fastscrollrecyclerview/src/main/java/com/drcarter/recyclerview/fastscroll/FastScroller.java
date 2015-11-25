package com.drcarter.recyclerview.fastscroll;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.drcarter.recyclerview.fastscroll.drawable.IndexDrawable;

public class FastScroller extends LinearLayout {

    private static final int HANDLE_HIDE_DELAY = 500;
    private static final int HANDLE_ANIMATION_DURATION = 500;
    private static final int TRACK_SNAP_RANGE = 5;
    private static final String ALPHA = "alpha";

    private ImageView viewTrack;
    private ImageView viewIndex;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private final HandleHider handleHider = new HandleHider();
    private final ScrollListener scrollListener = new ScrollListener();
    private int height;
    private boolean isTouchMoveEnable;

    private AnimatorSet currentAnimator;
    private OnFastScrollerIndexerLIstener listener;

    private boolean isAttachWindow;

    public FastScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        registerHandler(context);
    }

    public FastScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        registerHandler(context);
    }

    private void registerHandler(Context context) {
        setOrientation(HORIZONTAL);
        setClipChildren(false);
        LayoutInflater.from(context).inflate(R.layout.layout_fastscroller, this, true);
        this.viewTrack = (ImageView) findViewById(R.id.fastscroller_track);
        this.viewIndex = (ImageView) findViewById(R.id.fastscroller_index);

        setVisibility(View.GONE);
    }

    private void cancelHide() {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
        getHandler().removeCallbacks(handleHider);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.height = h;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (event.getX() >= 0
                        && event.getX() < getWidth()
                        && event.getY() >= viewIndex.getY()
                        && event.getY() < viewIndex.getY() + viewIndex.getHeight()) {
                    showHandle();
                    isTouchMoveEnable = true;
                    applyFastScrollerViewPosition();
                    return true;
                }
            }
            case MotionEvent.ACTION_MOVE: {
                if (isTouchMoveEnable) {
                    cancelHide();
                    setPosition(event.getY());
                    setRecyclerViewPosition(event.getY());
                    return true;
                }
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (isTouchMoveEnable) {
                    isTouchMoveEnable = false;
                    hideHandle();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setRecyclerView(@NonNull RecyclerView recyclerView, @NonNull LinearLayoutManager layoutManager) {
        this.recyclerView = recyclerView;
        this.layoutManager = layoutManager;
        this.recyclerView.addOnScrollListener(scrollListener);
    }

    private void setRecyclerViewPosition(float y) {
        if (recyclerView != null) {
            int itemCount = recyclerView.getAdapter().getItemCount();
            float proportion;
            if (viewTrack.getY() == 0) {
                proportion = 0f;
            } else if (viewTrack.getY() + viewTrack.getHeight() >= height - TRACK_SNAP_RANGE) {
                proportion = 1f;
            } else {
                proportion = y / (float) height;
            }
            int targetPos = getValueInRange(0, itemCount - 1, (int) (proportion * (float) itemCount));
            recyclerView.scrollToPosition(targetPos);
        }
    }

    private int getValueInRange(int min, int max, int value) {
        int minimum = Math.max(min, value);
        return Math.min(minimum, max);
    }

    private void setPosition(float y) {
        float position = y / height;
        int trackerHeight = viewTrack.getHeight();
        viewTrack.setY(getValueInRange(0, height - trackerHeight, (int) ((height - trackerHeight) * position)));

        int indexerHeight = viewIndex.getHeight();
        viewIndex.setY(getValueInRange(0, height - indexerHeight, (int) (viewTrack.getY() + (viewTrack.getHeight() / 2) - (viewIndex.getHeight() / 2))));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        isAttachWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        isAttachWindow = false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isAttached() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return isAttachedToWindow();
        } else {
            return isAttachWindow;
        }
    }

    private void showHandle() {
        cancelHide();
        if (viewIndex.getVisibility() == View.INVISIBLE) {
            AnimatorSet animatorSet = new AnimatorSet();
            viewIndex.setVisibility(VISIBLE);
            Animator alpha = ObjectAnimator.ofFloat(viewIndex, ALPHA, 0f, 1f).setDuration(HANDLE_ANIMATION_DURATION);
            animatorSet.playTogether(alpha);
            animatorSet.start();
        }
    }

    private void hideHandle() {
        currentAnimator = new AnimatorSet();
        viewIndex.setPivotX(viewIndex.getWidth());
        viewIndex.setPivotY(viewIndex.getHeight());
        Animator alpha = ObjectAnimator.ofFloat(viewIndex, ALPHA, 1f, 0f).setDuration(HANDLE_ANIMATION_DURATION);
        currentAnimator.playTogether(alpha);
        currentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (isAttached()) {
                    applyFastScrollerViewPosition();
                    viewIndex.setVisibility(INVISIBLE);
                    currentAnimator = null;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                viewIndex.setVisibility(INVISIBLE);
                currentAnimator = null;
            }
        });
        currentAnimator.start();
    }

    public void setIndexer(OnFastScrollerIndexerLIstener listener) {
        this.listener = listener;
    }

    public void hideIndexer() {
        getHandler().postDelayed(handleHider, HANDLE_HIDE_DELAY);
    }

    private class HandleHider implements Runnable {
        @Override
        public void run() {
            hideHandle();
        }
    }

    private class ScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            switch (newState) {
                case RecyclerView.SCROLL_STATE_DRAGGING: {
                    showHandle();
                    break;
                }
                case RecyclerView.SCROLL_STATE_IDLE: {
                    hideIndexer();
                    break;
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView rv, int dx, int dy) {
            applyFastScrollerViewPosition();
        }
    }

    private void applyFastScrollerViewPosition() {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

        if (totalItemCount > visibleItemCount) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }

        int position;
        if (pastVisibleItems == 0) {
            position = 0;
        } else if ((visibleItemCount + pastVisibleItems) == totalItemCount) {
            position = totalItemCount - 1;
        } else {
            position = pastVisibleItems;
        }
        float proportion = (float) position / (float) totalItemCount;
        setPosition(height * proportion);

        if (listener != null) {
            String index = listener.getIndexer(position);
            if (TextUtils.isEmpty(index)) {
                viewIndex.setImageDrawable(IndexDrawable.builder()
                        .textColor(getResources().getColor(android.R.color.white))
                        .fontSize(getResources().getDimensionPixelSize(R.dimen.fastscroller_index_text_size))
                        .build(index, getResources().getColor(android.R.color.transparent)));
            }
        }
    }
}
