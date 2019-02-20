package com.example.calendardemo.calendar.rule;

import com.example.calendardemo.calendar.CalendarConstantData;

/**
 * Created by lxc on 2019/2/20
 * e-mail ：18867762063@163.com
 */
public class RuleFactory {

    public static Rule createRepeatRule(int repeat) {
        Rule rule = null;
        switch (repeat){
            case CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_DAY:
                rule = new DayRule();
                break;
            case CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_WEEK:
                rule = new WeekRule();
                break;
            case CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_2_WEEK:
                rule = new TowWeekRule();
                break;
            case CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_MONTH:
                rule = new MonthRule();
                break;
            case CalendarConstantData.CALENDAR_EVENT_REPEAT_EVERY_YEAR:
                rule = new YearRule();
                break;
        }
        return rule;
    }
}
