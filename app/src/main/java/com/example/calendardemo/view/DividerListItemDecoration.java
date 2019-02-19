package com.example.calendardemo.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.calendardemo.R;
import com.example.calendardemo.application.TestApplication;

/**
 * Created by lxc on 2019/2/19
 * e-mail ：18867762063@163.com
 */
public class DividerListItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private int mDividerWidth;
    public static final int HORIZONTAL_LIST = 0;
    public static final int VERTICAL_LIST = 1;
    private int mOrientation;

    public DividerListItemDecoration(int height, int orientation) {
        this.mDividerWidth = height;
        this.mPaint = new Paint(1);
        this.mPaint.setColor(0);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.setOrientation(orientation);
    }

    public DividerListItemDecoration(int orientation) {
        this.mDividerWidth = 1;
        this.mPaint = new Paint(1);
        this.mPaint.setColor(TestApplication.getAppContext().getResources().getColor(R.color.gary));
        this.mPaint.setStyle(Paint.Style.FILL);
        this.setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != 0 && orientation != 1) {
            throw new IllegalArgumentException("invalid orientation");
        } else {
            this.mOrientation = orientation;
        }
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (this.mOrientation == 1) {
            this.drawVertical(c, parent);
        } else {
            this.drawHorizontal(c, parent);
        }

    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();

        for(int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            new RecyclerView(parent.getContext());
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + this.mDividerWidth;
            c.drawRect((float)left, (float)top, (float)right, (float)bottom, this.mPaint);
        }

    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();

        for(int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + this.mDividerWidth;
            c.drawRect((float)left, (float)top, (float)right, (float)bottom, this.mPaint);
        }

    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (this.mOrientation == 1) {
            outRect.set(0, 0, 0, this.mDividerWidth);
        } else {
            outRect.set(0, 0, this.mDividerWidth, 0);
        }

    }
}
