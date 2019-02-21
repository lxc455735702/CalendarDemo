package com.example.calendardemo.calendar;


import android.content.Context;
import android.util.Log;

import com.example.calendardemo.application.TestApplication;
import com.example.calendardemo.calendar.entity.CalendarEventPojo;
import com.example.calendardemo.calendar.rule.Rule;
import com.example.calendardemo.calendar.rule.RuleFactory;

import java.util.Calendar;
import java.util.List;

public class CalendarTest {

    private static final String TAG = "CalendarTest";

    public static void test() {
        testQueryCalendarEvent();
        testNextCalendar();
    }

    public static void testCheckAndAddCalendarAccounts() {
        long calendarId = SystemCalendarHandler.addCalendarAccount(TestApplication.getAppContext());
        Log.i(TAG, "testGetCalendarInfo calendarId=" + calendarId);
    }

    public static void testGetCalendarInfo() {
        SystemCalendarHandler.getCalendarInfo(TestApplication.getAppContext());
    }

    public static void testCheckCalendarAccounts() {
        long calendarId = SystemCalendarHandler.checkCalendarAccounts(TestApplication.getAppContext());
        Log.i(TAG, "testCheckCalendarAccounts calendarId=" + calendarId);
    }

    public static void testInsertCalendarEvent() {
        Context context = TestApplication.getAppContext();
        if (context == null) {
            Log.e(TAG, "testInsertCalendarEvent context -> error");
            return;
        }
        long calendarId = SystemCalendarHandler.checkCalendarAccounts(context);
        Log.i(TAG, "testInsertCalendarEvent calendarId=" + calendarId);

        long currentTimeMillis = System.currentTimeMillis();
        long currentTimeSecond = currentTimeMillis / 1000;
        try {
            List<Long> eventIdList = SystemCalendarHandler.insertCalendarEvent(
                    TestApplication.getAppContext(),
                    calendarId,
                    "title-1",
                    "location-1",
                    currentTimeSecond,
                    currentTimeSecond + 60 * 60,
                    0,
                    CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_DAY,
                    "description-1",
                    null,
                    0);
            if (null == eventIdList || eventIdList.size() == 0) {
                Log.i(TAG, "insertCalendarEvent eventIdList is null>error!");
                return;
            }
            for (Long eventId : eventIdList) {
                Log.i(TAG, "insertCalendarEvent eventId=" + eventId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<CalendarEventPojo> testQueryCalendarEvent() {
        long calendarId = SystemCalendarHandler.checkCalendarAccounts(TestApplication.getAppContext());
        Log.i(TAG, "testQueryCalendarEvent calendarId=" + calendarId);
        long startTimeSecond = System.currentTimeMillis() / 1000  - 60 * 60 * 24;
        long endTimeSecond = startTimeSecond + 5 * 24 * 60 * 60;
        List<CalendarEventPojo> list = SystemCalendarHandler.queryCalendarEvent(TestApplication.getAppContext(), calendarId, startTimeSecond, endTimeSecond);
        if (list == null ){
            return null;
        }
//        for (CalendarEventPojo eventPojo : list){
//           Log.d(TAG,"testQueryCalendarEvent eventPojo = " + eventPojo.toString());
//        }
        return list;
    }

    public static void testNextCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 1, 21);
        String currentDate = calendar.getTime().toString();
        Log.i(TAG, "testNextCalendar currentDate=" + currentDate);

        RuleFactory ruleFactory = new RuleFactory();

        Rule dayRule = ruleFactory.createRepeatRule(CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_DAY);
        Calendar dayCalendar = dayRule.getNextCalendar(calendar);
        String dayDate = dayCalendar.getTime().toString();
        Log.i(TAG, "testNextCalendar dayDate=" + dayDate);

        Rule monthRule = ruleFactory.createRepeatRule(CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_MONTH);
        Calendar monthCalendar = monthRule.getNextCalendar(calendar);
        String monthDate = monthCalendar.getTime().toString();
        Log.i(TAG, "testNextCalendar monthDate=" + monthDate);
    }
}
