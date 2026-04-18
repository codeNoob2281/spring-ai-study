package org.example.ai.entity;

import lombok.Data;

import java.time.LocalDateTime;

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
