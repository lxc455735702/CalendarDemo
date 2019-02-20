package com.example.calendardemo.calendar.rule;

import java.util.Calendar;

/**
 * Created by lxc on 2019/2/20
 * e-mail ：18867762063@163.com
 */
public interface Rule {

    /**
     * 获取重复规则下一个 Calendar
     *
     * @param lastTimeCalendar
     * @return
     */
    Calendar getNextCalendar(Calendar lastTimeCalendar);
}
