package com.v1.matripserver.journey.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public class JourneyResponseDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;

    // 등록한 사용자 관련
    private Long mid;
    private String mName;

    private List<JourneyImgRequestDto> journeyImgRequestDtoList;
}
