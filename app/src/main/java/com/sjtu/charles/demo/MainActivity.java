package com.sjtu.charles.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sjtu.charles.lib.adapter.RecyclerViewLoadMoreAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 关于RecyclerView上拉加载更多示例
 */
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewLoadMoreAdapter.OnLoadMoreListener, RecyclerViewAdapter.OnItemClickListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    boolean isLoading;
    private List<Map<String, Object>> data = new ArrayList<>();
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, data);
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        swipeRefreshLayout.setColorSchemeResources(R.color.blueStatus);
        swipeRefreshLayout.setOnRefreshListener(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setRecyclerView(recyclerView, swipeRefreshLayout);
        adapter.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(this);
        initData();
    }

    public void initData() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 1500);
    }

     /**
     * 获取测试数据
     */
    private void getData() {
        for (int i = 0; i < 6; i++) {
            Map<String, Object> map = new HashMap<>();
            data.add(map);
        }
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyItemRemoved(adapter.getItemCount());
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                data.clear();
                getData();
            }
        }, 2000);
    }

    @Override
    public void OnLoadMore() {
        if (!isLoading) {
            isLoading = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getData();
                    Log.d("test", "load more completed");
                    isLoading = false;
                }
            }, 1000);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this,"onItemClick:" + (position+1),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Toast.makeText(this,"onItemLongClick:" + (position+1),Toast.LENGTH_SHORT).show();
    }
}
