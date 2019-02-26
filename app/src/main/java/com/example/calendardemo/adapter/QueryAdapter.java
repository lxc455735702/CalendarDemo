package com.example.calendardemo.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.calendardemo.R;
import com.example.calendardemo.calendar.CalendarTools;
import com.example.calendardemo.calendar.entity.CalendarEventPojo;

import java.util.List;

/**
 * Created by lxc on 2019/2/21
 * e-mail ：18867762063@163.com
 */
public class QueryAdapter extends BaseQuickAdapter<CalendarEventPojo, BaseViewHolder> {

    public QueryAdapter(@Nullable List<CalendarEventPojo> data) {
        super(R.layout.item_query_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CalendarEventPojo item) {
        helper.setText(R.id.event_id, item.getEvent_id())
                .setText(R.id.system_id, item.getSystem_id())
                .setText(R.id.title, item.getTitle())
                .setText(R.id.location, item.getLocation())
                .setText(R.id.start_time, CalendarTools.getSsimpleDateFormatYMDHMS(item.getShowStartTime() * 1000))
                .setText(R.id.end_time, item.getShowEndTime() == 0 ?
                        "" : CalendarTools.getSsimpleDateFormatYMDHMS(item.getShowEndTime() * 1000))
                .setText(R.id.allday, item.getAll_day() + "")
                .setText(R.id.notes, item.getNotes())
                .setText(R.id.durationStr, item.getDurationStr())
                .setText(R.id.rrule, item.getRrule())
                .setText(R.id.index, "这是第" + getData().indexOf(item) + "个日历数据");
    }
}
