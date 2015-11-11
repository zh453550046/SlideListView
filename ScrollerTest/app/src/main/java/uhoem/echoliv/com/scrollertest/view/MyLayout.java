package uhoem.echoliv.com.scrollertest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 2015/11/10.
 */
public class MyLayout extends LinearLayout implements View.OnClickListener {

    private Scroller scroller;
    private VelocityTracker tracker;
    private boolean isShowAnimation, remove;
    private final int UNSCROLL = 0, SHOW_RIGHT = 1, HIDE_RIGHT = -1;
    private int width, scrollType = UNSCROLL;
    private float xDown, xMove;
    private List<MyLayout> list;

    public void setList(List<MyLayout> list) {
        this.list = list;
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        scroller = new Scroller(context);
    }

    public void setRightWidth(int width) {
        this.width = width;
    }

    public int getScrollType() {
        return scrollType;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
            invalidate();
        } else {
            if (scrollType != UNSCROLL) {
                isShowAnimation = false;
                if (scrollType == HIDE_RIGHT && remove) {
                    remove = false;
                    list.remove(this);
                    if (onHideRightListener != null) {
                        onHideRightListener.onHiderRight(true);
                    }
                } else {
                    if (scrollType == SHOW_RIGHT && list != null) {
                        for (MyLayout myLayout : list) {
                            if (myLayout != this && myLayout.getScrollType() == SHOW_RIGHT) {
                                myLayout.hideRight();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
//        switch (scrollType) {
//            case UNSCROLL:
//            case HIDE_RIGHT:
//                showRight();
//                break;
//            case SHOW_RIGHT:
//                hideRight();
//                break;
//            default:
//                break;
//        }
        Log.e("onclick", "onclick");
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                createTracker(ev);
                xMove = ev.getX();
                int distance = (int) (xMove - xDown);
                if (xMove - xDown < 0 && scroller.getCurrX() < width) {
                    if (scroller.getCurrX() + (-distance) >= width) {
                        distance = width - scroller.getCurrX();
                        scrollType = SHOW_RIGHT;
                    }
                    scroller.startScroll(scroller.getCurrX(), 0, Math.abs(distance), 0, 1);
                } else {
                    if (xMove - xDown >= 0 && scroller.getCurrX() > 0) {
                        if (scroller.getCurrX() - distance <= 0) {
                            distance = scroller.getCurrX();
                            scrollType = HIDE_RIGHT;
                        }
                        scroller.startScroll(scroller.getCurrX(), 0, -distance, 0, 1);
                    }
                }
                invalidate();
                xDown = xMove;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                if (shouldHideRight() || (scroller.getCurrX() > 0 && scroller.getCurrX() < width / 2)) {
//                    hideRight();
//                } else {
//                    if (shouldShowRight() || (scroller.getCurrX() >= width / 2 && scroller.getCurrX() < width)) {
//                        showRight();
//                    }
//                }
                if (shouldShowRight()) {
                    showRight();
                } else {
                    if (shouldHideRight()) {
                        hideRight();
                    } else if ((scroller.getCurrX() > 0 && scroller.getCurrX() < width / 2)) {
                        hideRight();
                    } else if (scroller.getCurrX() >= width / 2 && scroller.getCurrX() < width) {
                        showRight();
                    }
                }
                clearTracker();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    private boolean shouldHideRight() {
        if (tracker != null) {
            tracker.computeCurrentVelocity(1000);
            Log.e("shouldHideRight", "speed = " + tracker.getXVelocity());
            return tracker.getXVelocity() > 500;
        }
        return false;
    }

    private boolean shouldShowRight() {
        if (tracker != null) {
            tracker.computeCurrentVelocity(1000);
            Log.e("shouldShowRight", "speed = " + tracker.getXVelocity());
            return tracker.getXVelocity() < -500;
        }
        return false;
    }

    private void clearTracker() {
        if (tracker != null) {
            tracker.recycle();
            tracker = null;
        }
    }

    private void createTracker(MotionEvent ev) {
        if (tracker == null) {
            tracker = VelocityTracker.obtain();
        }
        tracker.addMovement(ev);
    }

    public void showRight() {
        if (isShowAnimation)
            return;
        isShowAnimation = true;
        scrollType = SHOW_RIGHT;
        scroller.startScroll(scroller.getCurrX(), 0, width - scroller.getCurrX(), 0, 300);
        invalidate();
    }

    public void hideRight() {
        if (isShowAnimation)
            return;
        isShowAnimation = true;
        scrollType = HIDE_RIGHT;
        scroller.startScroll(scroller.getCurrX(), 0, -scroller.getCurrX(), 0, 300);
        invalidate();
    }

    public void removeAfterhideRight(boolean remove) {
        this.remove = remove;
        if (remove) {
            hideRight();
        }
    }

    private OnHideRightListener onHideRightListener;

    public void setOnHideRightListener(OnHideRightListener onHideRightListener) {
        this.onHideRightListener = onHideRightListener;
    }

    public interface OnHideRightListener {
        void onHiderRight(boolean remove);
    }
}
