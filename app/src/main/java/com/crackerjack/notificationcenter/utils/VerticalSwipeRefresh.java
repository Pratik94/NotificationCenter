package com.crackerjack.notificationcenter.utils;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by pratik on 05/06/16.
 */
public class VerticalSwipeRefresh extends SwipeRefreshLayout {

    private float xDistance, yDistance, lastX, lastY;

    public VerticalSwipeRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);
                lastX = curX;
                lastY = curY;
                if (xDistance > yDistance)
                    return false;

        }

        return super.onInterceptTouchEvent(ev);
    }
}
