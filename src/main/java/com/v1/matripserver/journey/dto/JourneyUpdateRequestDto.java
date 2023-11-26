package com.v1.matripserver.journey.dto;

import java.time.LocalDate;
import java.util.List;

import com.v1.matripserver.util.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JourneyUpdateRequestDto {

    private Long id;
    private String title;
    private String content;
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer count;
    private float latitude;
    private float longitude;
    private Status status;
    private Long memberId;

    private List<JourneyImgRequestDto> journeyImgRequestDtoList;
}
