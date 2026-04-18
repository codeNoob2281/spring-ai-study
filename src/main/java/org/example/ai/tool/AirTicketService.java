package org.example.ai.tool;

import lombok.extern.slf4j.Slf4j;
import org.example.ai.entiry.AirTicket;
import org.example.ai.entiry.BookParam;
import org.example.ai.entiry.CityWeatherInfo;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 机票预订服务
 *
 * @author floyd
 */
@Service
@Slf4j
public class AirTicketService {


    @Tool(description = "查询可预订的机票")
    public List<AirTicket> queryTicketList(BookParam bookParam) {
        String fromCity = bookParam.getFromCity();
        String toCity = bookParam.getToCity();
        LocalDateTime bookTime = bookParam.getBookTime();
        log.info("开始查询[{}]到[{}]，[{}]后可预订的机票", fromCity, toCity, bookTime);
        ArrayList<AirTicket> airTickets = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date startTime = calendar.getTime();
        calendar.add(Calendar.HOUR, 2);
        Date endTime = calendar.getTime();
        airTickets.add(new AirTicket("杭州", "北京", startTime, endTime));
        return airTickets;
    }

    @Tool(description = "获取当前时间")
    public LocalDateTime getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        log.info("获取到当前时间为：{}", now);
        return now;
    }
}
