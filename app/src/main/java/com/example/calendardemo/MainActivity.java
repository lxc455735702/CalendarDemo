package com.example.calendardemo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.calendardemo.adapter.ContentAdapter;
import com.example.calendardemo.view.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    ContentAdapter mContentAdapter;
    List<String> mTestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
    }

    private void initData() {
        mTestList.add("添加行程");
        mTestList.add("删除行程");
        mTestList.add("修改行程");
        mTestList.add("查询行程");
        mContentAdapter = new ContentAdapter(mTestList);
        recyclerView.setAdapter(mContentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerListItemDecoration(5, DividerListItemDecoration.VERTICAL_LIST));
        mContentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mContentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "mContentAdapter onItemClick action = " + mTestList.get(position));
                Toast.makeText(MainActivity.this, "" + mTestList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
