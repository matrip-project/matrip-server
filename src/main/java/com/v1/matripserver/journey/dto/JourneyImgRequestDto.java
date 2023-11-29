package com.v1.matripserver.journey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JourneyImgRequestDto {

    private Long id;
    private String path;
    // 이미지 순서(0번이 대표 이미지)
    private Integer sequence;
}
