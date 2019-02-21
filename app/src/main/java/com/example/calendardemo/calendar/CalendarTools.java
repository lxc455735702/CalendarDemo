package com.example.calendardemo.calendar;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by lxc on 2019/2/20
 * e-mail ：18867762063@163.com
 */
public class CalendarTools {

    private static String TAG = "CalendarTools";

    private static SimpleDateFormat format_ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss

    /**
     * 秒级
     *
     * @param startTimeSecond 开始时间
     * @param endTimeSecond   结束时间
     * @return
     */

    public static String getEventDuration(long startTimeSecond, long endTimeSecond) {
        //todo 完善时间
        int duration = (int)((endTimeSecond - startTimeSecond) / 1000);
        return "P" + duration +"S";
    }

    /**
     * 获取事件重复规则
     *
     * @param repeat
     * @return
     */
    public static String getRRULE(int repeat, long deadline) {
        String rrule = null;
        switch (repeat) {
            // 每天
            case CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_DAY:
                rrule = CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_DAY_RRULE;
                break;
            // 每周
            case CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_WEEK:
                rrule = CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_WEEK_RRULE;
                break;
            // 每两周
            case CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_2_WEEK:
                rrule = CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_2_WEEK_RRULE;
                break;
            // 每月
            case CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_MONTH:
                rrule = CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_MONTH_RRULE;
                break;
            // 每年
            case CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_YEAR:
                rrule = CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_YEAR_RRULE;
                break;
            default:
                break;
        }
        String timeFormat = null;
        if (deadline != 0) {
            Calendar calendar = getCalendar(deadline);
            long startTimeOfDaySecond = getStartTimeOfDaySecond(calendar);
            timeFormat = getSsimpleDateFormatYMDHMS(startTimeOfDaySecond * 1000);
        }
        if (!TextUtils.isEmpty(timeFormat)) {
            rrule = rrule + "UNTIL=" + timeFormat + ";";
        }
        return rrule;
    }


    public static Calendar getCalendar(long second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(second * 1000);
        return calendar;
    }

    public static String getSsimpleDateFormatYMDHMS(long time){
        return format_ymdhms.format(new Date(time));
    }

    //获取当天（按当前传入的时区）00:00:00所对应时刻的long型值
    private static long getStartTimeOfDaySecond(Calendar calendar) {
        //todo 还需要测试
        long now = calendar.getTime().getTime();
        String timeZone = calendar.getTimeZone().toString();
        String tz = TextUtils.isEmpty(timeZone) ? "GMT+8" : timeZone;
        TimeZone curTimeZone = TimeZone.getTimeZone(tz);
        long oneDay = TimeUnit.DAYS.toMillis(1);//一天时间的毫秒值
        return (now - (now % oneDay) - curTimeZone.getRawOffset());
    }

}
