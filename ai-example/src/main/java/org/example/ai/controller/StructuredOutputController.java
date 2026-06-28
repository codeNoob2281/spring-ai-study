package org.example.ai.controller;

import jakarta.annotation.Resource;
import org.example.ai.convert.WeatherIInfoConverter;
import org.example.ai.entity.CityWeatherInfo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 结构化输出controller
 *
 * @author floyd
 */
@RestController
@RequestMapping("/structured-output")
public class StructuredOutputController {

    @Resource
    ChatClient defaultChatClient;

    @GetMapping("/get-weather-info-use-bean-converter")
    public CityWeatherInfo getWeatherInfoUseBeanConverter(String city) {
        return defaultChatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system("你是一个天气小助手，模拟输出天气信息")
                .user(u -> u.text("查询{city}的天气")
                        .param("city", city)
                )
                .call()
                .entity(CityWeatherInfo.class); // 底层使用BeanOutputConverter进行输出约束和转换
    }

    @GetMapping("/get-weather-info-use-map-converter")
    public Map<String, Object> getWeatherInfoUseMapConverter(String city) {
        return defaultChatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system("你是一个天气小助手，模拟输出天气信息")
                .user(u -> u.text("查询{city}的天气")
                        .param("city", city)
                )
                .call()
                .entity(new MapOutputConverter());
    }

    @GetMapping("/get-weather-info-use-list-converter")
    public List<String> getWeatherInfoUseListConverter(String city) {
        return defaultChatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system("你是一个天气小助手，模拟输出天气信息")
                .user(u -> u.text("查询{city}的天气")
                        .param("city", city)
                )
                .call()
                .entity(new ListOutputConverter());
    }

    @GetMapping("/get-weather-info-list")
    public List<CityWeatherInfo> getWeatherInfoList(String cityArray) {
        return defaultChatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system("你是一个天气小助手，模拟输出天气信息")
                .user(u -> u.text("查询{cityArray}的天气")
                        .param("cityArray", cityArray)
                )
                .call()
                .entity(new ParameterizedTypeReference<List<CityWeatherInfo>>() {
                });
    }

    @GetMapping("/get-weather-info-map-use-custom-converter")
    public Map<String, CityWeatherInfo> getWeatherInfoMapUseCustomConverter(String cityArray) {
        WeatherIInfoConverter converter = new WeatherIInfoConverter();
        return defaultChatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system("你是一个天气小助手，模拟输出天气信息")
                .user(u -> u.text("查询{cityArray}的天气")
                        .param("cityArray", cityArray)
                )
                .call()
                .entity(converter); // 使用自定义的Converter
    }


    @GetMapping("/get-weather-info-use-native-structured-output")
    public CityWeatherInfo getWeatherInfoUseNativeStructuredOutput(String city) {
        return defaultChatClient.prompt()
                // 启用原生结构化输出，实测doubao-seed-2-0-pro-260215不支持
                //.advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                .advisors(new SimpleLoggerAdvisor())
                .system("你是一个天气小助手，模拟输出天气信息")
                .user(u -> u.text("查询{city}的天气")
                        .param("city", city)
                )
                .call()
                .entity(CityWeatherInfo.class); // 底层使用BeanOutputConverter进行输出约束和转换
    }

}
