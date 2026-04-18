package org.example.ai.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * 机票订票信息
 *
 * @author floyd
 */
@Data
@AllArgsConstructor
public class AirTicket {

    private String fromCity;

    private String toCity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date departureTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date arrivalTime;
}
