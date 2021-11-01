package com.boylab.andlab.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView分割线
 * 可实现大部分标准列表显示
 */
public class RecyclerUtil {

    /**
     * RecyclerView 垂直 LayoutManager
     * @param context
     * @return
     */
    public static RecyclerView.LayoutManager verticalManager(Context context){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        return layoutManager;
    }

    /**
     * RecyclerView 水平 LayoutManager
     * @param context
     * @return
     */
    public static RecyclerView.LayoutManager horizontalManager(Context context){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        return layoutManager;
    }

    /**
     * RecyclerView 水平 GridLayoutManager
     * @param context
     * @param spanCount
     * @return
     */
    public static RecyclerView.LayoutManager gridLayoutManager(Context context, int spanCount){
        GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);
        return layoutManager;
    }

    /**
     * 垂直RecyclerView分割线
     * @param context
     * @param color     argb颜色
     * @param height
     */
    public static RecyclerView.ItemDecoration verticalDivider(Context context, int color, int height){
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        GradientDrawable dividerDrawable = new GradientDrawable();
        dividerDrawable.setColor(color);
        dividerDrawable.setSize(ViewGroup.LayoutParams.WRAP_CONTENT, height);
        horizontalDecoration.setDrawable(dividerDrawable);
        return horizontalDecoration;
    }

    /**
     * 水平RecyclerView分割线
     * @param context
     * @param color    argb颜色
     * @param width
     */
    public static RecyclerView.ItemDecoration horizontalDivider(Context context, int color, int width){
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL);
        GradientDrawable dividerDrawable = new GradientDrawable();
        dividerDrawable.setColor(color);
        dividerDrawable.setSize(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        horizontalDecoration.setDrawable(dividerDrawable);
        return horizontalDecoration;
    }

    /**
     * 注意：recyclerView 实现标准GridView
     */
    public static RecyclerView.ItemDecoration gridDivider(int color, boolean isDrawTop, boolean isDrawSide){
        return new GridDecoration(color, isDrawTop, isDrawSide);
    }

    /**
     * 注意：recyclerView 粗略实现非标准GridView
     * @param color
     */
    public static RecyclerView.ItemDecoration gridDivider(int color){
        return new GridDecoration(color);
    }

    /**
     * GridDecoration
     * RecyclerView实现GridView 时画线
     */
    public static class GridDecoration extends RecyclerView.ItemDecoration {

        private boolean isStandard = true;

        private int color = Color.GRAY;
        private boolean isDrawTop = false;
        private boolean isDrawSide = false; //左右两边

        private int offset = 2;
        private Paint mPaint;
        private Rect mRect = new Rect();

        /**
         * 粗略绘制实现右、下画线
         * @param color
         */
        public GridDecoration(int color) {
            this(color, 2, false, false);
            this.isStandard = false;
        }

        /**
         * 下列方法实现标准列表
         * @param color
         */
        public GridDecoration(int color, boolean isDrawTop, boolean isDrawSide) {
            this(color, 2, isDrawTop, isDrawSide);
        }

        public GridDecoration(int color, int offset, boolean isDrawTop, boolean isDrawSide) {
            this.color = color;
            this.offset = offset;
            this.isDrawTop = isDrawTop;
            this.isDrawSide = isDrawSide;
            initPaint();
        }

        private void initPaint(){
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(this.color);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(this.offset);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            for (int i = 0; i < parent.getChildCount(); i++) {
                View view = parent.getChildAt(i);
                //得到Rect
                parent.getDecoratedBoundsWithMargins(view, mRect);
                c.drawRect(mRect, mPaint);
            }
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            if (!isStandard){
                outRect.set(0, 0, offset, offset);
                return;
            }
            GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
            int size = parent.getChildCount();
            int spanCount = manager.getSpanCount();
            int row = size/spanCount;

            int height = isDrawTop ? offset : 0;
            int width = isDrawSide ? offset : 0;

            //得到View的位置
            int position = parent.getChildAdapterPosition(view);

            if (position < spanCount) {
                if (position == 0) {
                    outRect.set(width, height, offset, offset);
                }else if (position < spanCount-1){
                    outRect.set(0, height, offset, offset);
                }else{
                    outRect.set(0, height, width, offset);
                }
            }else if (position < row * spanCount){
                if (position % spanCount == 0) {
                    outRect.set(width, 0, offset, offset);
                }else if (position % spanCount < spanCount-1){
                    outRect.set(0, 0, offset, offset);
                }else{
                    outRect.set(0, 0, width, offset);
                }
            }else {
                if (position % spanCount == 0) {
                    outRect.set(width, 0, offset, offset);
                }else if (position % spanCount < spanCount-1){
                    outRect.set(0, 0, offset, offset);
                }else{
                    outRect.set(0, 0, width, offset);
                }
            }
        }
    }

}
