package com.v1.matripserver.journey.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JourneyResponseDto {

    private Long id;
    private String title;
    private String city;
    private String content;
    private String schedule;
    private Integer count;
    private float latitude;
    private float longitude;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Integer journeyCount;
    // 등록한 사용자 관련
    private Long mid;
    private String mName;

    private List<JourneyImgRequestDto> journeyImgRequestDtoList;
}
