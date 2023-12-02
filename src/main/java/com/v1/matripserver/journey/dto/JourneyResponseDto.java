package com.v1.matripserver.journey.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.v1.matripserver.util.entity.Status;

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
    private Status status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer count;
    private float latitude;
    private float longitude;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private Integer journeyCount;

    // 등록한 사용자 관련
    private Long memberId;
    private String memberEmail;
    private String memberName;
    private Integer memberAge;
    private char memberSex;


    private List<JourneyImgRequestDto> journeyImgRequestDtoList;
}
