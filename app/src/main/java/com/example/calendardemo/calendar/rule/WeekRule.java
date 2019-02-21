package com.example.calendardemo.calendar.rule;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by lxc on 2019/2/20
 * e-mail ：18867762063@163.com
 */
public class WeekRule implements Rule {
    @Override
    public Calendar getNextCalendar(Calendar lastTimeCalendar) {
        Log.d("WeekRule" ,"lastTimeCalendar = " + lastTimeCalendar.getTime().toString());
        Calendar copyCalendar = (Calendar)lastTimeCalendar.clone();
        copyCalendar.add(Calendar.DATE,7);
        Log.d("WeekRule" ,"copyCalendar = " + copyCalendar.getTime().toString());
        return copyCalendar;
    }
}
