package com.boylab.andlab.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boylab.andlab.R;
import com.boylab.andlab.utils.RecyclerUtil;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.header.TwoLevelHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RefreshRecycler extends RelativeLayout {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private ClassicsHeader mClassicsHeader = null;
    private OnRefreshCallBack onRefreshCallBack = null;

    private int freshDelay = 800;       //下拉动作、上拉动作时间
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("上次更新:yyyy-MM-dd HH:mm:ss");

    public RefreshRecycler(Context context) {
        this(context, null, 0);
    }

    public RefreshRecycler(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecycler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_recycler, this);

        refreshLayout = rootView.findViewById(R.id.refreshLayout);

        mClassicsHeader = new ClassicsHeader(context);
        mClassicsHeader.setTimeFormat(dateFormat);
        mClassicsHeader.setSpinnerStyle(SpinnerStyle.FixedBehind);
        mClassicsHeader.setPrimaryColor(Color.parseColor("#D9D9D9"));
        refreshLayout.setRefreshHeader(mClassicsHeader);

        ClassicsFooter mClassicsFooter = new ClassicsFooter(context);
        mClassicsFooter.setBackgroundColor(Color.parseColor("#D9D9D9"));
        mClassicsFooter.setSpinnerStyle(SpinnerStyle.FixedBehind);
        refreshLayout.setRefreshFooter(mClassicsFooter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                refreshLayout.finishRefresh(freshDelay);
                Log.i("___boylab>>>___", "onRefresh: "+String.format("%1$tF %1$tT %1$tL ", new Date()));
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("___boylab>>>___", "onRefresh: "+String.format("%1$tF %1$tT %1$tL ", new Date()));
                        if (onRefreshCallBack != null) {
                            onRefreshCallBack.onRefresh(RefreshRecycler.this);
                        }
                    }
                }, freshDelay);

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(freshDelay);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (onRefreshCallBack != null) {
                            onRefreshCallBack.onLoadMore(RefreshRecycler.this);
                        }
                    }
                }, freshDelay);
            }
        });

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(RecyclerUtil.verticalManager(context));
        recyclerView.addItemDecoration(RecyclerUtil.verticalDivider(context, Color.GRAY, 2));
    }

    public void setFreshDelay(int freshDelay) {
        this.freshDelay = freshDelay;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void setOnRefreshCallBack(OnRefreshCallBack onRefreshCallBack) {
        this.onRefreshCallBack = onRefreshCallBack;
    }

    public void setEnableRefresh(boolean enabled) {
        refreshLayout.setEnableRefresh(enabled);
    }

    public void setEnableLoadMore(boolean enabled) {
        refreshLayout.setEnableLoadMore(enabled);
    }

    public void setNoMoreData(boolean noMoreData) {
        refreshLayout.setNoMoreData(noMoreData);
    }

    public void setUpdateTime(Date time) {
        mClassicsHeader.setLastUpdateTime(time);
    }

    public interface OnRefreshCallBack {
        void onRefresh(@NonNull RefreshRecycler refreshRecycler);

        void onLoadMore(@NonNull RefreshRecycler refreshRecycler);
    }
}
