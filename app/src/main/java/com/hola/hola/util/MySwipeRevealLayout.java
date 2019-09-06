package com.hola.hola.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.chauthai.swipereveallayout.SwipeRevealLayout;

public class MySwipeRevealLayout extends SwipeRevealLayout {
    public MySwipeRevealLayout(Context context) {
        super(context);
    }

    public MySwipeRevealLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySwipeRevealLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() < 2) {
            throw new RuntimeException("Layout must have two children");
        }

        final LayoutParams params = getLayoutParams();

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int desiredWidth = 0;
        int desiredHeight = 0;

        int wrapChildHeight = 0;
        int wrapChildWidth = 0;
        int mDragEdge = getDragEdge();
        // first find the largest child
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final LayoutParams childParams = child.getLayoutParams();
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            if (childParams.height == LayoutParams.WRAP_CONTENT &&
                    (mDragEdge == DRAG_EDGE_LEFT || mDragEdge == DRAG_EDGE_RIGHT))
                wrapChildHeight = Math.max(child.getMeasuredHeight(), wrapChildHeight);
            else
                desiredHeight = Math.max(child.getMeasuredHeight(), desiredHeight);

            if (childParams.width == LayoutParams.WRAP_CONTENT &&
                    (mDragEdge == DRAG_EDGE_TOP || mDragEdge == DRAG_EDGE_BOTTOM))
                wrapChildWidth = Math.max(child.getMeasuredWidth(), wrapChildWidth);
            else
                desiredWidth = Math.max(child.getMeasuredWidth(), desiredWidth);

        }
        if (wrapChildHeight != 0) desiredHeight = wrapChildHeight;
        if (wrapChildWidth !=0) desiredWidth =wrapChildWidth;

        // create new measure spec using the largest child width
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(desiredWidth, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY);

        final int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final LayoutParams childParams = child.getLayoutParams();

            if (childParams != null) {
                if (childParams.height != LayoutParams.WRAP_CONTENT) {
                    childParams.height = measuredHeight;
                }

                if (childParams.width == LayoutParams.MATCH_PARENT) {
                    childParams.width = measuredWidth;
                }
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }

        // taking accounts of padding
        desiredWidth += getPaddingLeft() + getPaddingRight();
        desiredHeight += getPaddingTop() + getPaddingBottom();

        // adjust desired width
        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = measuredWidth;
        } else {
            if (params.width == LayoutParams.MATCH_PARENT) {
                desiredWidth = measuredWidth;
            }

            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = (desiredWidth > measuredWidth)? measuredWidth : desiredWidth;
            }
        }

        // adjust desired height
        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = measuredHeight;
        } else {
            if (params.height == LayoutParams.MATCH_PARENT) {
                desiredHeight = measuredHeight;
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = (desiredHeight > measuredHeight)? measuredHeight : desiredHeight;
            }
        }
        setDragEdge(mDragEdge);
        setMeasuredDimension(desiredWidth, desiredHeight);
    }
}
