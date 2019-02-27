package com.example.calendardemo.calendar;

import android.net.Uri;
import android.provider.CalendarContract;

/**
 * Created by lxc on 2019/2/19
 * e-mail ：18867762063@163.com
 */
public class CalendarConstantData {

    // 日历表Uri
    public static final Uri CALENDAR_URI = CalendarContract.Calendars.CONTENT_URI;
    //事件表Uri
    public static final Uri EVENT_URI = CalendarContract.Events.CONTENT_URI;
    //实例表Uri
    public static final Uri INSTANCE_URI = CalendarContract.Instances.CONTENT_URI;
    //提醒表Uri
    public static final Uri REMINDER_URI = CalendarContract.Reminders.CONTENT_URI;


    public static final String CALENDAR_EVENT_REPEAT_EVERY_DAY_RRULE = "FREQ=DAILY;";
    public static final String CALENDAR_EVENT_REPEAT_EVERY_WEEK_RRULE = "FREQ=WEEKLY;";
    public static final String CALENDAR_EVENT_REPEAT_EVERY_2_WEEK_RRULE = "FREQ=WEEKLY;INTERVAL=2;";
    public static final String CALENDAR_EVENT_REPEAT_EVERY_MONTH_RRULE = "FREQ=MONTHLY;";
    public static final String CALENDAR_EVENT_REPEAT_EVERY_YEAR_RRULE = "FREQ=YEARLY;";

    public static String CALENDARS_NAME = "test";
    public static String CALENDARS_ACCOUNT_NAME = "test@gmail.com";
    public static String CALENDARS_ACCOUNT_TYPE = "com.android.exchange";
    public static String CALENDARS_DISPLAY_NAME = "测试账户";

    public static String CAL_ACCESS_OWNER = "700";

    public static final int CALENDAR_EVENT_REPEAT_NEVER = 0;// 没有重复

    public static final int CALENDAR_EVENT_REPEAT_EVERY_DAY = 1;

    public static final int CALENDAR_EVENT_REPEAT_EVERY_WEEK = 2;

    public static final int CALENDAR_EVENT_REPEAT_EVERY_2_WEEK = 3;

    public static final int CALENDAR_EVENT_REPEAT_EVERY_MONTH = 4;

    public static final int CALENDAR_EVENT_REPEAT_EVERY_YEAR = 5;

    public static final int CALENDAR_EVENT_REPEAT_UNKNOWN = 6;//未知repeat规则，来自系统读取

    public static final int IS_ALL_DAY = 1; //是全天事件

    public static final int NOT_DELETE = 0 ;
}
