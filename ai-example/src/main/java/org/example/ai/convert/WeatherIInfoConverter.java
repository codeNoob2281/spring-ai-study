package org.example.ai.convert;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.example.ai.entity.CityWeatherInfo;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.ai.util.json.JsonParser;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * @author floyd
 */
@Slf4j
public class WeatherIInfoConverter implements StructuredOutputConverter<Map<String, CityWeatherInfo>> {

    @Nullable
    @Override
    public Map<String, CityWeatherInfo> convert(String source) {
        log.info("convert source: {}", source);
        return JsonParser.fromJson(source, new TypeReference<Map<String, CityWeatherInfo>>() {});
    }

    @Override
    public String getFormat() {
        return """
                输出标准的JSON格式字符串：
                  - key为城市名，例如：北京、上海
                  - value为一个json对象，包含属性city、weather和temperature，都是string类型的，属性包含中英文，例如"北京-Beijing"、温度包含摄氏度和华氏度，用|分隔，例如“16℃~27℃|16℉~27℉”
                """;
    }
}
