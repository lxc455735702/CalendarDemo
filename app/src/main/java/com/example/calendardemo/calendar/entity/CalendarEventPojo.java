package com.example.calendardemo.calendar.entity;

/**
 * Created by lxc on 2019/2/21
 * e-mail ：18867762063@163.com
 */
public class CalendarEventPojo {
    // 事件唯一id (创建时间戳)
    String event_id;
    // 事件在系统日历的id
    String system_id;
    // 标题
    String title;
    //地点
    String location;
    //开始时间（秒级）
    long start_time;
    //结束时间（秒级）
    long end_time;
    // 是否是全天事件
    int all_day;
    //是否同步到手机日历
    int sync_phone;
    //重复事件
    int repeats;
    // 详细信息
    String notes;
    //是否同步到服务端
    int sync_service;
    //是否被删除
    int id_deleted;
    //不生效的日期 用逗号分隔
    String invalid_days;
    //重复事件，截止日期 精确到秒
    long deadline;
    //是否是系统日历
    boolean systemEvent;

    String durationStr;

    String rrule;

    long showStartTime;

    long showEndTime;

    public CalendarEventPojo(long eventId, String system_id, String title, String location, long startTimeSecond,
                             long endTimeSecond, int allDay, int sync_phone, int repeat, String description,
                             int sync_service, int is_delete, String invalid_days, int deadline,
                             String durationStr, String rrule) {
        this.event_id = eventId + "";
        this.system_id = system_id;
        this.title = title;
        this.location = location;
        this.start_time = startTimeSecond;
        this.end_time = endTimeSecond;
        this.all_day = allDay;
        this.sync_phone = sync_phone;
        this.repeats = repeat;
        this.notes = description;
        this.sync_service = sync_service;
        this.id_deleted = is_delete;
        this.invalid_days = invalid_days;
        this.deadline = deadline;
        this.durationStr = durationStr;
        this.rrule = rrule;
    }

    public boolean isSystemEvent() {
        return systemEvent;
    }

    public void setSystemEvent(boolean systemEvent) {
        this.systemEvent = systemEvent;
    }

    public String getEvent_id() {
        return event_id;
    }


    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public int getAll_day() {
        return all_day;
    }

    public void setAll_day(int all_day) {
        this.all_day = all_day;
    }

    public int getSync_phone() {
        return sync_phone;
    }

    public void setSync_phone(int sync_phone) {
        this.sync_phone = sync_phone;
    }

    public int getRepeats() {
        return repeats;
    }

    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getSync_service() {
        return sync_service;
    }

    public void setSync_service(int sync_service) {
        this.sync_service = sync_service;
    }

    public int getId_deleted() {
        return id_deleted;
    }

    public void setId_deleted(int id_deleted) {
        this.id_deleted = id_deleted;
    }

    public String getInvalid_days() {
        return invalid_days;
    }

    public void setInvalid_days(String invalid_days) {
        this.invalid_days = invalid_days;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public String getDurationStr() {
        return durationStr;
    }

    public void setDurationStr(String durationStr) {
        this.durationStr = durationStr;
    }

    public String getRrule() {
        return rrule;
    }

    public void setRrule(String rrule) {
        this.rrule = rrule;
    }

    public long getShowStartTime() {
        return showStartTime;
    }

    public void setShowStartTime(long showStartTime) {
        this.showStartTime = showStartTime;
    }

    public long getShowEndTime() {
        return showEndTime;
    }

    public void setShowEndTime(long showEndTime) {
        this.showEndTime = showEndTime;
    }

    public void reBuildExtraParams() {

    }


    @Override
    public String toString() {
        return "CalendarEventPojo{" +
                "event_id='" + event_id + '\'' +
                ", system_id='" + system_id + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", all_day=" + all_day +
                ", sync_phone=" + sync_phone +
                ", repeats=" + repeats +
                ", notes='" + notes + '\'' +
                ", sync_service=" + sync_service +
                ", id_deleted=" + id_deleted +
                ", invalid_days='" + invalid_days + '\'' +
                ", deadline=" + deadline +
                ", systemEvent=" + systemEvent +
                ", durationStr='" + durationStr + '\'' +
                ", rrule='" + rrule + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CalendarEventPojo) {
            CalendarEventPojo calendarEventPojo = ((CalendarEventPojo) obj);
            if (this.getEvent_id().equals(calendarEventPojo.getEvent_id())) {
                // 每个日历的实例的strattime 开始时间是一样的，无法根据id 来区别是否一直 有可能是重复事件
                if (this.getShowStartTime() == calendarEventPojo.getShowStartTime()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }
}
