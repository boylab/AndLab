package com.boylab.andlab.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
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
     * <>为什么是add？</>
     * 注意：recyclerView.addItemDecoration(@NonNull ItemDecoration decor)
     * 即：分开添加verticalDivider、horizontalDivider即可
     */
    public static void gridDivider(Context context, int color, int offset, boolean isDrawTop, boolean isDrawSide){
        int dp2px = dp2px(context, offset);
        new GridDecoration(color, dp2px, isDrawTop, isDrawSide);
    }

    private static int dp2px(Context context, int dp){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        //float fontScale = displayMetrics.scaledDensity;
        int px = (int) (dp * density + 0.5f);
        return px;
    }

    /**
     * GridDecoration
     * RecyclerView实现GridView 时画线
     */
    public static class GridDecoration extends RecyclerView.ItemDecoration {

        private int color = Color.GRAY;
        private int offset = 4;

        private Paint mPaint;
        private Rect mRect = new Rect();

        private boolean isDrawTop = false;
        private boolean isDrawSide = false; //左右两边

        public GridDecoration(int color, int offset) {
            this(color, offset, false, false);
        }

        public GridDecoration(boolean isDrawTop, boolean isDrawSide) {
            this(Color.GRAY, 4, isDrawTop, isDrawSide);
        }

        public GridDecoration(int color, int offset, boolean isDrawTop, boolean isDrawSide) {
            this.color = color;
            this.offset = offset;
            this.isDrawTop = isDrawTop;
            this.isDrawSide = isDrawSide;
            initPaint();
        }

        public GridDecoration setColor(int color, int offset) {
            this.color = color;
            this.offset = offset;
            initPaint();
            return this;
        }

        public GridDecoration setDrawSide(boolean isDrawTop, boolean isDrawSide) {
            this.isDrawTop = isDrawTop;
            this.isDrawSide = isDrawSide;
            return this;
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
