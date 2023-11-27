package com.v1.matripserver.journey.dto;

import java.time.LocalDate;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.v1.matripserver.util.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    //페이지 번호
    private int page;

    //페이지 당 데이터 개수
    private int size;

    //검색 내용
    private String keyword;

    // 필터링
    // 여행 도시
    private String city;

    // 여행 시작 날짜, 종료 날짜
    private LocalDate startDate;
    private LocalDate endDate;

    // 여행 상태 여부
    private String status;

    // 성별
    private char sex;

    // 나이
    private Integer age;

   public PageRequestDTO() {
        page=1;
        size=10;
        keyword=null;
        city=null;
        startDate=null;
        endDate=null;
        status= "ACTIVE";
        sex='\0';
        age=null;
    }

    //page와 size를 가지고 Pageable 객체를 생성해주는 메서드
    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1, size, sort);
    }
}
