package com.example.calendardemo.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;

import com.example.calendardemo.calendar.entity.CalendarEventPojo;
import com.example.calendardemo.calendar.rule.Rule;
import com.example.calendardemo.calendar.rule.RuleFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static java.lang.Long.parseLong;


public class SystemCalendarHandler {

    private static final String TAG = "SystemCalendarHandler";

    /**
     * 获取日历ID
     *
     * @param context
     * @return 日历ID
     */
    public static long checkAndAddCalendarAccounts(Context context) {
        long oldId = checkCalendarAccounts(context);
        if (oldId >= 0) {
            return oldId;
        } else {
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccounts(context);
            } else {
                return -1;
            }
        }
    }

    /**
     * 获取日历信息
     */
    public static void getCalendarInfo(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(CalendarConstantData.CALENDAR_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                long calID = cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars._ID));
                String name = null;
                if (cursor.getColumnIndex(CalendarContract.Calendars.NAME) != -1) {
                    name = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.NAME));
                }
                String displayName = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME));
                String accountName = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME));
                String ownerName = null;
                if (cursor.getColumnIndex(CalendarContract.Calendars.OWNER_ACCOUNT) != -1) {
                    ownerName = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.OWNER_ACCOUNT));
                }
                String accountType = null;
                if (cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE) != -1) {
                    accountType = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE));
                }
                String accessLevel = null;
                if (cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL) != -1) {
                    accessLevel = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL));
                }

                Log.i(TAG, "calID=" + calID + "\nname=" + name + "\ndisplayName=" + displayName + "\naccountName=" + accountName + "\nownerName=" + ownerName
                        + "\naccountType=" + accountType + "\naccessLevel=" + accessLevel);
                Log.i(TAG, "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
    }

    /**
     * 检查是否存在日历账户
     *
     * @param context
     * @return
     */
    public static long checkCalendarAccounts(Context context) {

        Cursor userCursor = context.getContentResolver()
                .query(CalendarConstantData.CALENDAR_URI, null, null, null, CalendarContract.Calendars._ID + " ASC ");
        try {
            if (userCursor == null)//查询返回空值
                return -1;
            int count = userCursor.getCount();
            if (count > 0) {//存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    /**
     * 添加一个日历账户
     *
     * @param context
     * @return
     */
    public static long addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CalendarConstantData.CALENDARS_NAME);

        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CalendarConstantData.CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarConstantData.CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CalendarConstantData.CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarConstantData.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CalendarConstantData.CALENDARS_ACCOUNT_NAME);

        Uri calendarUri = CalendarConstantData.CALENDAR_URI.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CalendarConstantData.CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarConstantData.CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    /**
     * 插入日历事件
     *
     * @param context
     * @param calendarId
     * @param title
     * @param location
     * @param startTimeSecond
     * @param endTimeSecond
     * @param allDay
     * @param repeats
     * @param notes
     * @return
     */
    public static List<Long> insertCalendarEvent(Context context, long calendarId, String title, String location,
                                                 long startTimeSecond, long endTimeSecond, int allDay, int repeats,
                                                 String notes, String invalidDays, long deadline) {
        Log.d(TAG, "insertCalendarEvent");
        if (deadline != 0 && startTimeSecond > deadline) {
            Log.d(TAG, "insertCalendarEvent startTimeSecond=" + startTimeSecond + ",deadline=" + deadline);
            return null;
        }
        List<ContentValues> eventList = new ArrayList<>();
        if (repeats == CalendarConstantData.CALENDAR_EVENT_REPEAT_NEVER) {
            ContentValues event = buildCommonEventContentValues(calendarId, title, location, allDay, notes);

            event.put(CalendarContract.Events.DTSTART, startTimeSecond * 1000);//必须有
            event.put(CalendarContract.Events.DTEND, endTimeSecond * 1000);

            eventList.add(event);
        } else if (TextUtils.isEmpty(invalidDays)) {
            ContentValues event = buildCommonEventContentValues(calendarId, title, location, allDay, notes);

            event.put(CalendarContract.Events.DTSTART, startTimeSecond * 1000);//必须有
            //重复事件不可以写endTime，只能写duration
            String duration = CalendarTools.getEventDuration(startTimeSecond, endTimeSecond);
            String rrule = CalendarTools.getRRULE(repeats, deadline);

            event.put(CalendarContract.Events.DURATION, duration);
            event.put(CalendarContract.Events.RRULE, rrule);

            eventList.add(event);
        } else {
            Log.d(TAG, "invalidDays=" + invalidDays + ",deadline=" + deadline);
            Map<Long, Long> splitMap = new HashMap<>();
            String[] spliteArray = invalidDays.split(",");
            //存入map注意去重
            for (String splite : spliteArray) {
                long spliteLong = parseLong(splite);
                if (spliteLong >= startTimeSecond
                        && (deadline == 0 || (deadline != 0 && spliteLong <= deadline))) {
                    splitMap.put(spliteLong, spliteLong);
                }
            }
            List<Long> splitLongList = new ArrayList<>();
            for (Map.Entry<Long, Long> entry : splitMap.entrySet()) {
                splitLongList.add(entry.getValue());
            }
            Collections.sort(splitLongList);

            RuleFactory ruleFactory = new RuleFactory();
            Rule rule = ruleFactory.createRepeatRule(repeats);
            List<StartTimeAndDeadline> startTimeAndDeadlineList = new ArrayList<>();
            for (int i = 0; i < splitLongList.size(); i++) {
                if (i == 0) {//开始时间到第一个不生效时间
                    long newStartTimeSecond = startTimeSecond;
                    long newDeadlineSecond = splitLongList.get(0);

                    if (newStartTimeSecond == newDeadlineSecond) {
                        continue;
                    }
                    StartTimeAndDeadline startTimeAndDeadline = new StartTimeAndDeadline();
                    startTimeAndDeadline.setStartTimeSecond(newStartTimeSecond);
                    startTimeAndDeadline.setDeadline(newDeadlineSecond);
                    startTimeAndDeadlineList.add(startTimeAndDeadline);
                } else {//前一个不生效时间到当前不生效时间
                    long lastSplitTimeSecond = splitLongList.get(i - 1);
                    Calendar lastSplitCalendar = CalendarTools.getCalendar(lastSplitTimeSecond);
                    Calendar newStartTimeCalendar = rule.getNextCalendar(lastSplitCalendar);
                    long newStartTimeSecond = newStartTimeCalendar.getTimeInMillis() / 1000;
                    long newDeadlineSecond = splitLongList.get(i);

                    StartTimeAndDeadline startTimeAndDeadline = new StartTimeAndDeadline();
                    startTimeAndDeadline.setStartTimeSecond(newStartTimeSecond);
                    startTimeAndDeadline.setDeadline(newDeadlineSecond);
                    startTimeAndDeadlineList.add(startTimeAndDeadline);
                }
            }

            // 加上最后一个不生效日期到最后截止时间
            long lastTimeSecond = splitLongList.get(splitLongList.size() - 1);
            Calendar lastTimeCalendar = CalendarTools.getCalendar(lastTimeSecond);
            Calendar newStartTimeCalendar = rule.getNextCalendar(lastTimeCalendar);
            long newStartTimeSecond = newStartTimeCalendar.getTimeInMillis() / 1000;
            long newDeadlineSecond = deadline;

            //开始时间等于截止时间，不用插入
            if (newStartTimeSecond == newDeadlineSecond) {
                return new ArrayList<Long>();
            }
            StartTimeAndDeadline startTimeAndDeadline = new StartTimeAndDeadline();
            startTimeAndDeadline.setStartTimeSecond(newStartTimeSecond);
            startTimeAndDeadline.setDeadline(newDeadlineSecond);
            startTimeAndDeadlineList.add(startTimeAndDeadline);

            if (null == startTimeAndDeadlineList || startTimeAndDeadlineList.size() == 0) {
                Log.d(TAG, "insertCalendarEvent startTimeAndDeadlineList is null>error!");
                return null;
            }
            for (StartTimeAndDeadline item : startTimeAndDeadlineList) {
                long itemStartTime = item.getStartTimeSecond();
                long itemDeadline = item.getDeadline();
                ContentValues event = buildCommonEventContentValues(calendarId, title, location, allDay, notes);
                event.put(CalendarContract.Events.DTSTART, itemStartTime * 1000);//必须有
                //重复事件不可以写endTime，只能写duration
                String duration = CalendarTools.getEventDuration(startTimeSecond, endTimeSecond);
                String rrule = CalendarTools.getRRULE(repeats, itemDeadline);

                event.put(CalendarContract.Events.DURATION, duration);
                event.put(CalendarContract.Events.RRULE, rrule);

                eventList.add(event);
            }

        }

        if (null == eventList || eventList.size() == 0) {
            Log.d(TAG, "insertCalendarEvent eventList is null>error!");
            return null;
        }
        List<Long> eventIdList = new ArrayList<>();
        for (ContentValues event : eventList) {
            Uri newEventUri = context.getContentResolver().insert(CalendarConstantData.EVENT_URI, event);
            long eventId = newEventUri == null ? -1 : ContentUris.parseId(newEventUri);
            Log.d(TAG,"insertCalendarEvent eventId = " + eventId);
            eventIdList.add(eventId);
        }
        return eventIdList;
    }

    /**
     * 更新日历事件
     * @param context
     * @param title
     * @param location
     * @param startTimeSecond
     * @param endTimeSecond
     * @param allDay
     * @param repeats
     * @param notes
     * @return
     */
    public static List<Long> updateCalendarEvent(Context context, String systemId, String title, String location,
                                          long startTimeSecond, long endTimeSecond,  int allDay, int repeats,
                                          String notes, String invalidDays, long deadline){
        long calendarId = checkAndAddCalendarAccounts(context);
        if(calendarId < 0){
            Log.d(TAG, "updateCalendarEvent calendarId = " + calendarId + " >error!");
            return null;
        }
        //先删除日历事件
        deleteCalendarEvent(context, systemId);
        //再插入日历事件
        return insertCalendarEvent(context, calendarId, title, location, startTimeSecond,
                endTimeSecond, allDay, repeats, notes, invalidDays, deadline);
    }


    /**
     * 删除日历事件
     *
     * @param context
     * @param systemId
     * @return
     */
    public static int deleteCalendarEvent(Context context, String systemId) {
        List<Long> systemIdList = getSpliteSystemIdList(systemId);
        if (null == systemIdList || systemIdList.size() == 0) {
            return -1;
        }
        for (long itemSystemId : systemIdList) {
            Uri deleteUri = ContentUris.withAppendedId(CalendarConstantData.EVENT_URI, itemSystemId);
            int rows = context.getContentResolver().delete(deleteUri, null, null);
        }
        return 0;
    }

    /**
     * 插入日历事件提醒
     *
     * @param context
     * @param systemId
     * @return
     */
    private static long insertEventReminder(Context context, long systemId) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, systemId);
        int reminderMinutes = 0;
        // 提前几分钟有提醒
        values.put(CalendarContract.Reminders.MINUTES, reminderMinutes);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri newReminderUri = context.getContentResolver().insert(CalendarConstantData.REMINDER_URI, values);
        long reminderId = newReminderUri == null ? -1 : ContentUris.parseId(newReminderUri);
        return reminderId;
    }

    /**
     * 删除日历事件提醒
     *
     * @param context
     * @param systemId
     * @return
     */
    private static int deleteEventReminder(Context context, String systemId) {
        List<Long> systemIdList = getSpliteSystemIdList(systemId);
        if (null == systemIdList || systemIdList.size() == 0) {
            return -1;
        }
        for (long itemSystemId : systemIdList) {
            Uri deleteUri = ContentUris.withAppendedId(CalendarConstantData.REMINDER_URI, itemSystemId);
            int rows = context.getContentResolver().delete(deleteUri, null, null);
        }
        return 0;
    }

    /**
     * 向日历中添加一个事件和提醒
     * @param context
     * @param title
     * @param location
     * @param startTimeSecond
     * @param endTimeSecond
     * @param allDay
     * @param notes
     * @return 返回eventId
     */
    public static OnCalendarListener insertCalendarEventAndReminder(Context context, String title, String location,
                                           long startTimeSecond, long endTimeSecond,  int allDay,
                                           int repeats, String notes, String invalidDays, long deadline){
        OnCalendarListener onCalendarListener = new OnCalendarListener();
        // 1.获取日历ID
        long calendarId = checkAndAddCalendarAccounts(context);
        if(calendarId < 0){
            onCalendarListener.setStatus(OnCalendarListener.Status._CALENDAR_ERROR);
            return onCalendarListener;
        }

        // 2.向日历中添加事件
        List<Long> eventList = insertCalendarEvent(context, calendarId, title, location, startTimeSecond,
                endTimeSecond, allDay, repeats, notes, invalidDays, deadline);
        if(null == eventList){
            onCalendarListener.setStatus(OnCalendarListener.Status._EVENT_ERROR);
            return onCalendarListener;
        }
        if(eventList.size() == 0){//不用插入系统日历事件
            onCalendarListener.setStatus(OnCalendarListener.Status._SUCCESS);
            return onCalendarListener;
        }

        List<OnCalendarListener.EventIdAndReminderId> eventIdAndReminderIdList = new ArrayList<>();
        for(Long eventId : eventList){
            // 3.为事件设定提醒
            long reminderId = insertEventReminder(context, eventId);
            if(reminderId < 0){
                onCalendarListener.setStatus(OnCalendarListener.Status._REMIND_ERROR);
                return onCalendarListener;
            }
            onCalendarListener.setStatus(OnCalendarListener.Status._SUCCESS);

            OnCalendarListener.EventIdAndReminderId eventIdAndReminderId = new OnCalendarListener.EventIdAndReminderId();
            eventIdAndReminderId.setEventId(eventId);
            eventIdAndReminderId.setReminderId(reminderId);

            eventIdAndReminderIdList.add(eventIdAndReminderId);
        }
        onCalendarListener.setEventIdAndReminderIdList(eventIdAndReminderIdList);

        return onCalendarListener;
    }

    /**
     * 删除事件和提醒
     *
     * @param context
     * @param systemId
     */
    public static OnCalendarListener deleteCalendarEventAndReminder(Context context, String systemId) {
        OnCalendarListener onCalendarListener = new OnCalendarListener();
        // 1.获取日历ID
        long calendarId = checkAndAddCalendarAccounts(context);
        if (calendarId < 0) {
            onCalendarListener.setStatus(OnCalendarListener.Status._CALENDAR_ERROR);
            return onCalendarListener;
        }
        // 2.删除日历事件
        int eventRows = deleteCalendarEvent(context, systemId);
        if (eventRows < 0) {
            onCalendarListener.setStatus(OnCalendarListener.Status._EVENT_ERROR);
            return onCalendarListener;
        }
        // 3.删除日历事件提醒
        int reminderRows = deleteEventReminder(context, systemId);
        if (reminderRows < 0) {
            onCalendarListener.setStatus(OnCalendarListener.Status._REMIND_ERROR);
            return onCalendarListener;
        }
        onCalendarListener.setStatus(OnCalendarListener.Status._SUCCESS);
        List<Long> systemIdList = getSpliteSystemIdList(systemId);
        if (null == systemIdList || systemIdList.size() == 0) {
            Log.d(TAG, "deleteCalendarEventAndReminder systemIdList is null>error!");
            return onCalendarListener;
        }
        List<OnCalendarListener.EventIdAndReminderId> eventIdAndReminderIdList = new ArrayList<>();
        for (long itemSystemId : systemIdList) {
            OnCalendarListener.EventIdAndReminderId eventIdAndReminderId = new OnCalendarListener.EventIdAndReminderId();
            eventIdAndReminderId.setEventId(itemSystemId);
            eventIdAndReminderIdList.add(eventIdAndReminderId);
        }
        onCalendarListener.setEventIdAndReminderIdList(eventIdAndReminderIdList);
        return onCalendarListener;
    }

    /**
     * 查询日历事件
     *
     * @param context
     * @param queryStartTimeSecond
     * @param queryEndTimeSecond
     * @return
     */
    public static List<CalendarEventPojo> queryCalendarEvent(Context context, long calendarId, long queryStartTimeSecond, long queryEndTimeSecond){
        if(queryStartTimeSecond < 0 || queryEndTimeSecond < 0 || queryStartTimeSecond > queryEndTimeSecond){
            Log.d(TAG, "queryCalendarEventGroupByCalendar startTimeSecond="
                    + queryStartTimeSecond + ",endTimeSecond=" + queryEndTimeSecond);
            return null;
        }
        long queryStartTimeMillis = queryStartTimeSecond * 1000;
        long queryEndTimeMillis = queryEndTimeSecond * 1000;
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, queryStartTimeMillis);
        ContentUris.appendId(builder, queryEndTimeMillis);
        Cursor cursor = context.getContentResolver().query(builder.build(), null, null, null, CalendarContract.Instances.BEGIN + " ASC ");
        List<CalendarEventPojo> calendarEventPojoList = null;
        try{
            if(cursor.moveToFirst()){
                calendarEventPojoList = new ArrayList<>();
                do{
                    long eventId = cursor.getLong(cursor.getColumnIndex(CalendarContract.Instances.EVENT_ID));//系统日历事件
                    long systemId = eventId;
                    String title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));
                    String location = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                    long curStartTimeMillis = cursor.getLong(cursor.getColumnIndex(CalendarContract.Instances.BEGIN));
                    int allDay = cursor.getInt(cursor.getColumnIndex(CalendarContract.Events.ALL_DAY));
                    String description = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                    String durationStr = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DURATION));
                    long curEndTimeMillis = cursor.getLong(cursor.getColumnIndex(CalendarContract.Instances.END));
                    String rrule = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.RRULE));
                    long startTimeMillis = cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART));
                    long endTimeMillis = cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTEND));

                    long startTimeSecond = startTimeMillis / 1000;
                    long endTimeSecond = endTimeMillis / 1000;

                    long curStartTimeSecond = curStartTimeMillis / 1000;
                    long duration = -1;
                    if(!TextUtils.isEmpty(durationStr)){
                        try{
                            duration = CalendarTools.RFC2445ToMilliseconds(durationStr);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if(curEndTimeMillis == 0 && duration != -1){
                        curEndTimeMillis = curStartTimeMillis + duration;
                    }
                    long curEndTimeSecond = curEndTimeMillis / 1000;

                    if(allDay == CalendarConstantData.IS_ALL_DAY){
                        startTimeSecond = dealAllDayStartTime(startTimeSecond);
                        curStartTimeSecond = dealAllDayStartTime(curStartTimeSecond);
                        endTimeSecond = dealAllDaysEndTime(endTimeSecond);
                        curEndTimeSecond = dealAllDaysEndTime(curEndTimeSecond);
                    }
                    Log.d(TAG,"rrule = " + rrule);
                    int repeat = CalendarTools.getRepeatByRRULE(rrule);
                    CalendarEventPojo calendarEventPojo = new CalendarEventPojo(eventId, systemId + "",
                            title, location, startTimeSecond, endTimeSecond, allDay, 1,
                            repeat, description, 0,
                            CalendarConstantData.NOT_DELETE, null, 0,durationStr, rrule);
                    calendarEventPojo.setSystemEvent(true);
                    calendarEventPojo.setShowStartTime(curStartTimeSecond);
                    calendarEventPojo.setShowEndTime(curEndTimeSecond);
                    calendarEventPojo.reBuildExtraParams();
                    calendarEventPojoList.add(calendarEventPojo);
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return  calendarEventPojoList;
    }

    public static class OnCalendarListener {
        public enum Status {
            _CALENDAR_ERROR,
            _EVENT_ERROR,
            _REMIND_ERROR,
            _SUCCESS
        }

        public static class EventIdAndReminderId {
            long eventId;
            long reminderId;

            public long getEventId() {
                return eventId;
            }

            public void setEventId(long eventId) {
                this.eventId = eventId;
            }

            public long getReminderId() {
                return reminderId;
            }

            public void setReminderId(long reminderId) {
                this.reminderId = reminderId;
            }
        }

        Status status;
        List<EventIdAndReminderId> eventIdAndReminderIdList;

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public List<EventIdAndReminderId> getEventIdAndReminderIdList() {
            return eventIdAndReminderIdList;
        }

        public void setEventIdAndReminderIdList(List<EventIdAndReminderId> eventIdAndReminderIdList) {
            this.eventIdAndReminderIdList = eventIdAndReminderIdList;
        }
    }

//    /**
//     * 辅助方法：获取设置时间起止时间的对应毫秒数
//     * @param year
//     * @param month 1-12
//     * @param day 1-31
//     * @param hour 0-23
//     * @param minute 0-59
//     * @return
//     */
//    public static long remindTimeCalculator(int year,int month,int day,int hour,int minute){
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year,month-1,day,hour,minute);
//        return calendar.getTimeInMillis();
//    }

    //===================================== 私有方法 ===============================================

    /**
     * 构造日历事件共有参数
     *
     * @param calendarId
     * @param title
     * @param location
     * @param allDay
     * @return
     */
    private static ContentValues buildCommonEventContentValues(long calendarId, String title,
                                                               String location, int allDay, String notes) {
        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, calendarId);

        event.put(CalendarContract.Events.TITLE, title);
        event.put(CalendarContract.Events.EVENT_LOCATION, location);
        event.put(CalendarContract.Events.ALL_DAY, allDay);
        event.put(CalendarContract.Events.DESCRIPTION, notes);
        event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());//这个是时区，必须有
        return event;
    }

    /**
     * 递归获取拆分后的起始时间
     *
     * @param rule
     * @param calendar
     * @param spliteCalendar
     * @return
     */
   /* private static Calendar getSpliteStartTime(Rule rule, Calendar calendar, Calendar spliteCalendar){
        if(CalendarTools.compareYMD(calendar, spliteCalendar) == 1){
            return calendar;
        }else{
            Calendar nextCalendar = rule.getNextCalendar(calendar);
            return getSpliteStartTime(rule, nextCalendar, spliteCalendar);
        }
    }*/

    private static class StartTimeAndDeadline {
        long startTimeSecond;
        long deadline;

        public long getStartTimeSecond() {
            return startTimeSecond;
        }

        public void setStartTimeSecond(long startTimeSecond) {
            this.startTimeSecond = startTimeSecond;
        }

        public long getDeadline() {
            return deadline;
        }

        public void setDeadline(long deadline) {
            this.deadline = deadline;
        }
    }

    /**
     * 获取系统eventId列表
     *
     * @param systemId
     * @return
     */
    private static List<Long> getSpliteSystemIdList(String systemId) {
        if (TextUtils.isEmpty(systemId)) {
            return null;
        }
        String[] systemIdArray = systemId.split(",");
        if (null == systemIdArray || systemIdArray.length == 0) {
            return null;
        }
        List<Long> spliteSystemIdList = new ArrayList<>();
        for (String systemIdStr : systemIdArray) {
            try {
                long itemSystemId = parseLong(systemIdStr);
                spliteSystemIdList.add(itemSystemId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return spliteSystemIdList;

    }

    /**
     * 处理全天事件开始时间
     * 系统全天时间举例：
     * 创建时间为5月19日
     * 开始时间变成了5月19日8点（猜测是东八区）
     * 结束时间变成了5月20日8点
     * 由于这种情况造成事件列表19日和20日都出现了这个事件，这是不合理的。
     * 所以对系统全天事件的开始时间和结束时间做处理
     * @param timeSecond
     */
    private static long dealAllDayStartTime(long timeSecond){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeSecond * 1000);
        return CalendarTools.getStartTimeOfDaySecond(calendar);
    }

    /**
     * 处理全天事件结束时间
     * 系统全天时间举例：
     * 创建时间为5月19日
     * 开始时间变成了5月19日8点（猜测是东八区）
     * 结束时间变成了5月20日8点
     * 由于这种情况造成事件列表19日和20日都出现了这个事件，这是不合理的。
     * 所以对系统全天事件的开始时间和结束时间做处理
     * @param timeSecond
     */
    private static long dealAllDaysEndTime(long timeSecond){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeSecond * 1000);
        return CalendarTools.getEndTimeOfLastDaySecond(calendar);
}

}
