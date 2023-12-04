package com.v1.matripserver.journey.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.v1.matripserver.journey.entity.Journey;
import com.v1.matripserver.util.entity.Status;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

    // 동행 정보를 가지고 상품 이미지 정보(한 개)와 댓글의 개수를 구해주는 메서드 페이지 단위로 구하기
    @Query("select j.id, j.title, j.city, j.status, j.startDate, j.endDate, j.memberId, ji, count(j.id) from Journey j left outer join JourneyImg ji on ji.journeyId = j" +
        " where ji.status = :journeyImgStatus and ji.sequence = 0"
        + " and (:keyword is null or j.title like %:keyword%)"
        + " and (:city is null or j.city = :city)"
        + " and ((:journeyStatus is null and (j.status = 'ACTIVE' or j.status = 'CLOSED')) or j.status = :journeyStatus)"
        + " and ((:startDate is null and :endDate is null) or (j.startDate <= :startDate and j.endDate >= :endDate))"
        + " and j.memberId.status = :journeyImgStatus"
        + " and ((:startYear=0 and :endYear=0) or (YEAR (j.memberId.birth) BETWEEN :startYear and :endYear))"
        + " group by j")
    Page<Object[]> readJourneyList(Pageable pageable, String keyword, String city,
        LocalDate startDate, LocalDate endDate, Status journeyStatus, Status journeyImgStatus, Integer startYear, Integer endYear);

    // 하나의 동행 게시글 아이디를 가지고 이미지 정보를 구해주는 메서드
    @Query("select j, ji, count(c.id) from Journey j"
        + " left outer join JourneyImg ji on ji.journeyId = j"
        + " left outer join Comment c on c.journeyId = j"
        + " left join c.parentId p"
        + " where (j.status = 'ACTIVE' or j.status = 'CLOSED') and (ji is null or (ji is not null and ji.status = 'ACTIVE')) and j.id = :id"
        + " and (c is null or (c is not null and c.status = 'ACTIVE'))"
        + " and (p is null or (p is not null and p.status = 'ACTIVE'))"
        + " group by j, ji"
        + " order by ji.sequence asc")
    List<Object[]> readJourney(Long id);


    // 마이페이지 작성한 글 조회
    @Query("select j.id, j.title, j.city, j.status, j.startDate, j.endDate, j.memberId, ji from Journey j left outer join JourneyImg ji on ji.journeyId = j"
        + " where j.memberId.id = :memberId and (j.status = 'ACTIVE' or j.status = 'CLOSED')"
        + " group by j")
    List<Object[]> readMyPageJourney(Long memberId);
}
