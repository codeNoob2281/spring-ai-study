package org.example.ai.tool;

import org.example.ai.enums.TimeField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeServiceTest {

    private DateTimeService dateTimeService;

    @BeforeEach
    void setUp() {
        dateTimeService = new DateTimeService();
    }

    private String formatCurrentTime(TimeField field, int delta) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        addField(calendar, field, delta);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    private void addField(Calendar calendar, TimeField field, int delta) {
        switch (field) {
            case YEAR -> calendar.add(Calendar.YEAR, delta);
            case MONTH -> calendar.add(Calendar.MONTH, delta);
            case WEEK -> calendar.add(Calendar.WEEK_OF_YEAR, delta);
            case DAY -> calendar.add(Calendar.DAY_OF_MONTH, delta);
            case HOUR -> calendar.add(Calendar.HOUR, delta);
            case MINUTE -> calendar.add(Calendar.MINUTE, delta);
            case SECOND -> calendar.add(Calendar.SECOND, delta);
        }
    }

    @Test
    void calculateTime_addZeroDelta_returnsCurrentTime() {
        String result = dateTimeService.calculateTime(TimeField.DAY, 0);
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    void calculateTime_addOneDay() {
        String result = dateTimeService.calculateTime(TimeField.DAY, 1);
        String expected = formatCurrentTime(TimeField.DAY, 1);
        assertEquals(expected, result);
    }

    @Test
    void calculateTime_subtractOneDay() {
        String result = dateTimeService.calculateTime(TimeField.DAY, -1);
        String expected = formatCurrentTime(TimeField.DAY, -1);
        assertEquals(expected, result);
    }

    @Test
    void calculateTime_addOneYear() {
        String result = dateTimeService.calculateTime(TimeField.YEAR, 1);
        String expected = formatCurrentTime(TimeField.YEAR, 1);
        assertEquals(expected, result);
    }

    @Test
    void calculateTime_addOneMonth() {
        String result = dateTimeService.calculateTime(TimeField.MONTH, 1);
        String expected = formatCurrentTime(TimeField.MONTH, 1);
        assertEquals(expected, result);
    }

    @Test
    void calculateTime_addOneWeek() {
        String result = dateTimeService.calculateTime(TimeField.WEEK, 1);
        String expected = formatCurrentTime(TimeField.WEEK, 1);
        assertEquals(expected, result);
    }

    @Test
    void calculateTime_addOneHour() {
        String result = dateTimeService.calculateTime(TimeField.HOUR, 1);
        String expected = formatCurrentTime(TimeField.HOUR, 1);
        assertEquals(expected, result);
    }

    @Test
    void calculateTime_addOneMinute() {
        String result = dateTimeService.calculateTime(TimeField.MINUTE, 1);
        String expected = formatCurrentTime(TimeField.MINUTE, 1);
        assertEquals(expected, result);
    }

    @Test
    void calculateTime_addOneSecond() {
        String result = dateTimeService.calculateTime(TimeField.SECOND, 1);
        String expected = formatCurrentTime(TimeField.SECOND, 1);
        assertEquals(expected, result);
    }

    @Test
    void calculateTime_largeDelta() {
        String result = dateTimeService.calculateTime(TimeField.YEAR, 100);
        String expected = formatCurrentTime(TimeField.YEAR, 100);
        assertEquals(expected, result);
    }
}
