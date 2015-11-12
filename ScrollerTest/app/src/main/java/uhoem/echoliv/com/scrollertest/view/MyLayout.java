package uhoem.echoliv.com.scrollertest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import java.util.List;

/**
 * Created by Administrator on 2015/11/10.
 */
public class MyLayout extends LinearLayout implements AbsListView.OnScrollListener {

    private Scroller scroller;
    private VelocityTracker tracker;
    private boolean isShowAnimation, remove;
    private final int UNSCROLL = 0, SHOW_RIGHT = 1, HIDE_RIGHT = -1;
    private int width, scrollType = UNSCROLL;
    private float xDown, yDown, xMove, yMove;
    private List<MyLayout> list;

    public void setList(List<MyLayout> list) {
        this.list = list;
    }

    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
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
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getX();
                yDown = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                createTracker(ev);
                xMove = ev.getX();
                yMove = ev.getY();
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
                Log.e("y", yMove - yDown + "");
                if ((yMove - yDown < 10 && yMove - yDown > -10) || isYLowSpeed()) {
                    yDown = yMove;
                    return true;
                } else {
                    yDown = yMove;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
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


    public boolean isYLowSpeed() {
        if (tracker != null) {
            tracker.computeCurrentVelocity(1000);
            return tracker.getYVelocity() < 500 && tracker.getYVelocity() > -500;
        }
        return false;
    }

    private boolean shouldHideRight() {
        if (tracker != null) {
            tracker.computeCurrentVelocity(1000);
            return tracker.getXVelocity() > 900;
        }
        return false;
    }

    private boolean shouldShowRight() {
        if (tracker != null) {
            tracker.computeCurrentVelocity(1000);
            return tracker.getXVelocity() < -900;
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (list != null) {
            for (MyLayout myLayout : list) {
                if (myLayout.getScrollType() == SHOW_RIGHT) {
                    myLayout.hideRight();
                    break;
                }
            }
        }
    }


    public interface OnHideRightListener {
        void onHiderRight(boolean remove);
    }
}
