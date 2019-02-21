package com.example.calendardemo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.calendardemo.adapter.ContentAdapter;
import com.example.calendardemo.calendar.CalendarConstantData;
import com.example.calendardemo.calendar.CalendarTest;
import com.example.calendardemo.calendar.SystemCalendarHandler;
import com.example.calendardemo.view.DividerListItemDecoration;
import com.hb.dialog.myDialog.MyAlertInputDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    ContentAdapter mContentAdapter;
    List<String> mTestList = new ArrayList<>();

    private MyAlertInputDialog myAlertInputDialog;

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
        mTestList.add("获取日历信息");
        mTestList.add("测试日历下一个");
        mContentAdapter = new ContentAdapter(mTestList);
        recyclerView.setAdapter(mContentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerListItemDecoration(5, DividerListItemDecoration.VERTICAL_LIST));
        mContentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mContentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String actionString = mTestList.get(position);
                Log.d(TAG, "mContentAdapter onItemClick action = " + actionString);
                Toast.makeText(MainActivity.this, "" + actionString, Toast.LENGTH_SHORT).show();
//                SystemCalendarHandler.getCalendarInfo(view.getContext());
                long calId = SystemCalendarHandler.checkAndAddCalendarAccounts(view.getContext()); //获取日历账户的id
                if (calId < 0) { //获取账户id失败直接返回，添加日历事件失败
                    return;
                }
                if ("添加行程".equals(actionString)) {
                    new MyThread().start();
                } else if ("测试日历下一个".equals(actionString)) {
                    CalendarTest.testNextCalendar();
                } else if ("删除行程".equals(actionString)) {
                    showDeleteCalendarInputDialog();
                } else if ("修改行程".equals(actionString)) {

                } else if ("查询行程".equals(actionString)) {

                }
            }
        });
    }


    private void showDeleteCalendarInputDialog() {
        if (myAlertInputDialog == null) {
            myAlertInputDialog = new MyAlertInputDialog(MainActivity.this).builder()
                    .setTitle("请输入")
                    .setEditText("请输入要删除的行程id");
            myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputString = myAlertInputDialog.getContentEditText().getText().toString();
                    Log.d(TAG, "showDeleteCalendarInputDialog  onClick inputString = " + inputString);
                    SystemCalendarHandler.deleteCalendarEvent(MainActivity.this, inputString);
                    myAlertInputDialog.dismiss();
                }
            }).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myAlertInputDialog.dismiss();
                }
            });
        }
        myAlertInputDialog.getContentEditText().setText("");
        myAlertInputDialog.show();
    }

    public class MyThread extends Thread {

        //继承Thread类，并改写其run方法

        public void run() {
            Log.d(TAG, "run");
            long startTime = System.currentTimeMillis() / 1000;
            long endTime = startTime + 60 * 10;
            long calendarId = SystemCalendarHandler.checkCalendarAccounts(MainActivity.this);
            SystemCalendarHandler.insertCalendarEvent(MainActivity.this, calendarId, "测试",
                    "test", startTime, endTime, 0, CalendarConstantData.CALENDAR_EVENT_REPEAT_NEVER,
                    "lxc测试的数据", null, 0);
//            CalendarTest.testInsertCalendarEvent();
        }
    }

}
