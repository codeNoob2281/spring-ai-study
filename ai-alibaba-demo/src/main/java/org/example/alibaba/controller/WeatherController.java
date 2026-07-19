package org.example.alibaba.controller;

import org.example.alibaba.entity.CityWeatherInfo;
import org.example.alibaba.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public CityWeatherInfo getWeather(@PathVariable String city) {
        return weatherService.queryWeather(city);
    }
}
