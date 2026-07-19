package org.example.alibaba.service;

import org.example.alibaba.entity.CityWeatherInfo;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private final Map<String, CityWeatherInfo> weatherData = new HashMap<>();

    public WeatherService() {
        weatherData.put("北京", new CityWeatherInfo("北京", "晴", "28℃", "45%", "东南风 3级"));
        weatherData.put("上海", new CityWeatherInfo("上海", "多云", "26℃", "65%", "东风 2级"));
        weatherData.put("广州", new CityWeatherInfo("广州", "雷阵雨", "32℃", "80%", "南风 4级"));
        weatherData.put("深圳", new CityWeatherInfo("深圳", "阵雨", "30℃", "75%", "西南风 3级"));
        weatherData.put("杭州", new CityWeatherInfo("杭州", "晴转多云", "27℃", "60%", "东南风 2级"));
        weatherData.put("成都", new CityWeatherInfo("成都", "阴", "25℃", "70%", "北风 1级"));
        weatherData.put("武汉", new CityWeatherInfo("武汉", "晴", "29℃", "55%", "南风 3级"));
        weatherData.put("西安", new CityWeatherInfo("西安", "多云", "26℃", "50%", "东北风 2级"));
        weatherData.put("南京", new CityWeatherInfo("南京", "晴", "28℃", "58%", "东南风 3级"));
        weatherData.put("重庆", new CityWeatherInfo("重庆", "多云", "30℃", "68%", "西南风 2级"));
    }

    @Tool(description = "查询指定城市的天气信息，包括天气状况、温度、湿度和风力")
    public CityWeatherInfo queryWeather(@ToolParam(description = "要查询天气的城市名称，例如：北京、上海、广州") String city) {
        CityWeatherInfo info = weatherData.get(city);
        if (info == null) {
            return new CityWeatherInfo(city, "未知", "未知", "未知", "未知");
        }
        return info;
    }
}
