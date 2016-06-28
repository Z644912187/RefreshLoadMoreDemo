package com.sjtu.charles.lib.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
                    setLoading(true);
                    onLoadMoreListener.OnLoadMore();
                }
            }
        });
    }

    public void setLoading(boolean flag) {
        if (mLLloading != null) {
            mLLloading.setVisibility(flag?View.VISIBLE:View.GONE);
        }
        if (mTvLoadingMore != null) {
            mTvLoadingMore.setVisibility(flag?View.GONE:View.VISIBLE);
        }
    }

    /**
     * 加载结束提示信息
     * @param text 低部显示提示信息
     */
    public void setLoadedHint(CharSequence text) {
        if (mLLloading != null) {
            mLLloading.setVisibility(View.GONE);
        }
        if (mTvLoadingMore != null) {
            mTvLoadingMore.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(text)) {
                mTvLoadingMore.setText(text);
            } else {
                mTvLoadingMore.setText(R.string.load_no_more);
            }
        }
    }

    public void setLoadedHint() {
        setLoadedHint(null);
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
        if (holder instanceof FootViewHolder) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onLoadMoreListener != null) {
                        setLoading(true);
                        onLoadMoreListener.OnLoadMore();
                    }
                }
            });
            mTvLoadingTitle = ((FootViewHolder) holder).loadingTitle;
            mPbLoading = ((FootViewHolder) holder).loadingPb;
            mTvLoadingMore = ((FootViewHolder) holder).loadingMore;
            mLLloading = ((FootViewHolder) holder).llLoading;
        }
    }

    private View mPbLoading;
    private TextView mTvLoadingTitle;
    private TextView mTvLoadingMore;
    private LinearLayout mLLloading;

    class FootViewHolder extends ViewHolder {

        ProgressBar loadingPb;
        TextView loadingTitle;
        TextView loadingMore;
        LinearLayout llLoading;
        public FootViewHolder(View view) {
            super(view);
            llLoading = (LinearLayout) view.findViewById(R.id.ll_loading_more);
            loadingTitle = (TextView) view.findViewById(R.id.tv_load_title);
            loadingPb = (ProgressBar) view.findViewById(R.id.pb_load_animate);
            loadingMore = (TextView) view.findViewById(R.id.tv_load_no_more);
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
