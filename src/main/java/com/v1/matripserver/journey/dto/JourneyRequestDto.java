package com.v1.matripserver.journey.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JourneyRequestDto {

    private Long id;
    private String title;
    private String content;
    private String schedule;
    private Integer count;
    private float latitude;
    private float longitude;
    private Long memberId;

    private List<String> imagePathList;
}
