package com.v1.matripserver.journey.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.v1.matripserver.journey.entity.Journey;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

    // 상품 정보를 가지고 상품 이미지 정보와 댓글의 개수를 구해주는 메서드 페이지 단위로 구하기
    @Query("select j, ji from Journey j left outer join JourneyImg ji on ji.journeyId = j" +
        " where j.status = 'ACTIVE' and (:keyword is null or j.title like %:keyword%) group by j")
    Page<Object[]> readJourneyList(Pageable pageable, String keyword);

    // 하나의 상품 아이디를 가지고 상품 이미지 정보와 댓글의 개수를 구해주는 메서드
    @Query("select j, ji from Journey j left outer join JourneyImg ji on ji.journeyId = j" +
        " where j.status = 'ACTIVE' and j = :id group by j")
    List<Object[]> readJourney(Long id);
}
