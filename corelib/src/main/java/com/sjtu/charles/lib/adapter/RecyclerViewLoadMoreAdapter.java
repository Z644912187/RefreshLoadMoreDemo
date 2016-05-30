package com.sjtu.charles.lib.adapter;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wordoor.andr.corelib.R;

public class RecyclerViewLoadMoreAdapter extends Adapter<ViewHolder> {

    private static final int TYPE_FOOTER = 0x11001;
    protected RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;

    public void setRecyclerView(RecyclerView recyclerView) {
        setRecyclerView(recyclerView,null);
    }

    public void setRecyclerView(RecyclerView recyclerView, final SwipeRefreshLayout swipeRefreshLayout) {
        this.recyclerView = recyclerView;
        layoutManager = recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = -1;
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                }
                if (swipeRefreshLayout != null) {
                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        notifyItemRemoved(getItemCount());
                        return;
                    }
                }
                if (lastVisibleItemPosition + 1 == getItemCount() && onLoadMoreListener != null) {
                    onLoadMoreListener.OnLoadMore();
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    static class FootViewHolder extends ViewHolder {
        public FootViewHolder(View view) {
            super(view);
        }
    }

    public interface OnLoadMoreListener {
        void OnLoadMore();
    }

    private OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
}