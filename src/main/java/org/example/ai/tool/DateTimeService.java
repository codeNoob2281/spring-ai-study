package org.example.ai.tool;

import lombok.extern.slf4j.Slf4j;
import org.example.ai.enums.TimeField;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author floyd
 */
@Service
@Slf4j
public class DateTimeService {

    @Tool(description = "Calculate time based on the given time field and delta value.")
    public String calculateTime(TimeField timeField, int deltaValue) {
        log.info("开始计算时间，时间字段为：{}，delta值为：{}", timeField, deltaValue);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        switch (timeField) {
            case YEAR -> calendar.add(Calendar.YEAR, deltaValue);
            case MONTH -> calendar.add(Calendar.MONTH, deltaValue);
            case WEEK -> calendar.add(Calendar.WEEK_OF_YEAR, deltaValue);
            case DAY -> calendar.add(Calendar.DAY_OF_MONTH, deltaValue);
            case HOUR -> calendar.add(Calendar.HOUR, deltaValue);
            case MINUTE -> calendar.add(Calendar.MINUTE, deltaValue);
            case SECOND -> calendar.add(Calendar.SECOND, deltaValue);
            default -> throw new IllegalArgumentException("Invalid time field: " + timeField);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = dateFormat.format(calendar.getTime());
        log.info("计算完成，结果为：{}", timeStr);
        return timeStr;
    }

    @Tool(description = "Set an alarm for the specified time, the `calculateTime` function should be called if you don't know the real time")
    public void setAlarm(@ToolParam(description = "The time to set the alarm for. The format should be 'yyyy-MM-dd HH:mm:ss'") String time) {
        log.info("已设置闹钟：{}", time);
    }
}
