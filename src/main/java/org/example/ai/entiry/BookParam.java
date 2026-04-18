package org.example.ai.entiry;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author floyd
 */
@Data
public class BookParam {

    String fromCity;
    String toCity;
    LocalDateTime bookTime;
}
