package com.sjtu.charles.lib.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wordoor.andr.corelib.R;


public class RecyclerViewLoadMoreAdapter extends Adapter<ViewHolder> {

    private static final int TYPE_FOOTER = 0x11001;

    public void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(-1)) {
                    onScrolledToTop();
                } else if (!recyclerView.canScrollVertically(1)) {
                    onScrolledToBottom();
                } else if (dy < 0) {
                    onScrolledUp();
                } else if (dy > 0) {
                    onScrolledDown();
                }
            }
            public void onScrolledUp() {}

            public void onScrolledDown() {}

            public void onScrolledToTop() {}

            public void onScrolledToBottom() {
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.OnLoadMore();
                    setLoading(true);
                }
            }
        });
    }

    public void setLoading(boolean flag) {
        if (view != null)
            view.setVisibility(flag?View.VISIBLE:View.GONE);
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

    View view;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot, parent, false);
            setLoading(false);
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