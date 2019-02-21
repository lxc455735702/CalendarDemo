package com.example.calendardemo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.calendardemo.adapter.ContentAdapter;
import com.example.calendardemo.adapter.QueryAdapter;
import com.example.calendardemo.calendar.CalendarTest;
import com.example.calendardemo.calendar.entity.CalendarEventPojo;
import com.example.calendardemo.view.DividerListItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by lxc on 2019/2/21
 * e-mail ：18867762063@163.com
 */
public class CalendarInfoActivity extends BaseActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    QueryAdapter queryAdapter;

    List<CalendarEventPojo> calendarEventPojoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_calendar_info);
        calendarEventPojoList = CalendarTest.testQueryCalendarEvent();
        queryAdapter = new QueryAdapter(calendarEventPojoList);
        recyclerView.setAdapter(queryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerListItemDecoration(5, DividerListItemDecoration.VERTICAL_LIST));
    }

}
