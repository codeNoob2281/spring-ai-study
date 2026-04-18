package org.example.ai.tool;

import org.example.ai.entity.CityWeatherInfo;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

/**
 * @author floyd
 */
@Service
public class WeatherService {


    @Tool(description = "查询天气信息")
    public CityWeatherInfo queryWeather(@ToolParam(description = "城市名称") String city) {
        if ("北京".equals(city)) {
            return new CityWeatherInfo("北京", "晴", "13℃");
        } else if ("上海".equals(city)) {
            return new CityWeatherInfo("上海", "晴", "23℃");
        } else {
            throw new RuntimeException("暂不支持该城市");
        }
    }
}
