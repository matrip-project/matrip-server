package com.v1.matripserver.journey.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.v1.matripserver.journey.dto.JourneyImgRequestDto;
import com.v1.matripserver.journey.dto.JourneyRequestDto;
import com.v1.matripserver.journey.dto.JourneyResponseDto;
import com.v1.matripserver.journey.dto.JourneyUpdateRequestDto;
import com.v1.matripserver.journey.dto.PageRequestDTO;
import com.v1.matripserver.journey.dto.PageResponseDTO;
import com.v1.matripserver.journey.entity.Journey;
import com.v1.matripserver.journey.entity.JourneyImg;
import com.v1.matripserver.journey.repository.JourenyImgRepository;
import com.v1.matripserver.journey.repository.JourneyRepository;
import com.v1.matripserver.member.entity.Member;
import com.v1.matripserver.member.service.MemberService;
import com.v1.matripserver.util.entity.Status;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class JourneyService {

    private final JourneyRepository journeyRepository;

    private final JourenyImgRepository jourenyImgRepository;

    private final MemberService memberService;

    // 동행 게시글 작성
    public void createJourney(JourneyRequestDto journeyRequestDto) {

        Member member = memberService.getMemberById(journeyRequestDto.getMemberId());

        // 메서드로 수정이 필요
        // 동행 게시글 저장
        Journey journey = Journey.builder()
            .title(journeyRequestDto.getTitle())
            .content(journeyRequestDto.getContent())
            .city(journeyRequestDto.getCity())
            .count(journeyRequestDto.getCount())
            .startDate(journeyRequestDto.getStartDate())
            .endDate(journeyRequestDto.getEndDate())
            .latitude(journeyRequestDto.getLatitude())
            .longitude(journeyRequestDto.getLongitude())
            .member(member)
            .build();

        journey.setStatus(Status.ACTIVE);

        journeyRepository.save(journey);

        // 동행 게시글 이미지 저장
        List<JourneyImg> journeyImgList = new ArrayList<>();

        for (JourneyImgRequestDto journeyImgRequestDto : journeyRequestDto.getJourneyImgRequestDtoList()){

            // 메서드로 수정
            JourneyImg journeyImg = JourneyImg.builder()
                .path(journeyImgRequestDto.getPath())
                .journey(journey)
                .sequence(journeyImgRequestDto.getSequence())
                .build();
            journeyImg.setStatus(Status.ACTIVE);
            journeyImgList.add(journeyImg);
        }

        if (!journeyImgList.isEmpty()){
            jourenyImgRepository.saveAll(journeyImgList);
        }
    }

    // 동행 게시글 조회
    public PageResponseDTO<JourneyResponseDto, Object[]> readJourneyList(PageRequestDTO pageRequestDTO){
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("id").descending());

        int startYear = 0;
        int endYear = 0;

        if (pageRequestDTO.getAge() != null){
            endYear = LocalDate.now().getYear() - pageRequestDTO.getAge();
            startYear = endYear - 9;
        }

        // 검색할 시작 연도와 끝 연도 계산
        Page<Object []> result = journeyRepository.readJourneyList(pageable, pageRequestDTO.getKeyword(), pageRequestDTO.getCity(),
            pageRequestDTO.getStartDate(), pageRequestDTO.getEndDate(), Status.valueOf(pageRequestDTO.getStatus()), Status.ACTIVE,
            startYear, endYear);

        Function<Object [], JourneyResponseDto> fn = (arr -> {
            Journey journey = Journey.builder()
                .id((Long) arr[0])
                .title((String) arr[1])
                .city((String) arr[2])
                .status((Status) arr[3])
                .startDate((LocalDate) arr[4])
                .endDate((LocalDate) arr[5])
                .member((Member) arr[6])
                .build();

            JourneyImg journeyImg = (JourneyImg) arr[7];

            Integer count = ((Long) arr[8]).intValue();

            if (journeyImg == null) {
                // ProductImage가 없는 경우에 대한 처리
                return entitiesToDTO(journey, Collections.emptyList(), count);
            } else {
                // ProductImage가 있는 경우에 대한 처리
                return entitiesToDTO(journey, Collections.singletonList(journeyImg), count);
            }
        });

        return new PageResponseDTO<>(result, fn);
    }

    // 검색 결과 반환
    JourneyResponseDto entitiesToDTO(Journey journey, List<JourneyImg> journeyImgList, Integer count){

        List<JourneyImgRequestDto> journeyImgRequestDtoList = journeyImgList.stream().filter(Objects::nonNull)
            .map(journeyImg -> {
                return JourneyImgRequestDto.builder()
                    .id(journeyImg.getId())
                    .path(journeyImg.getPath())
                    .sequence(journeyImg.getSequence())
                    .build();
            }).toList();

        return JourneyResponseDto.builder()
            .id(journey.getId())
            .title(journey.getTitle())
            .content(journey.getContent())
            .count(journey.getCount())
            .city(journey.getCity())
            .startDate(journey.getStartDate())
            .endDate(journey.getEndDate())
            .latitude(journey.getLatitude())
            .longitude(journey.getLongitude())
            .journeyImgRequestDtoList(journeyImgRequestDtoList)
            .createDt(journey.getCreated())
            .updateDt(journey.getUpdated())
            .journeyCount(count)
            .memberId(journey.getMemberId().getId())
            .memberEmail(journey.getMemberId().getEmail())
            .memberName(journey.getMemberId().getName())
            .memberAge(journey.getMemberId().getAge())
            .memberSex(journey.getMemberId().getSex())
            .build();
    }

    // 동행 게시글 상세 조회
    public JourneyResponseDto readJourney(Long id){

        List<Object []> result = journeyRepository.readJourney(id);

        Journey journey = (Journey)result.get(0)[0];

        List<JourneyImg> journeyImgList = new ArrayList<>();

        result.forEach(arr -> {
            JourneyImg journeyImg = (JourneyImg) arr[1];
            journeyImgList.add(journeyImg);
        });

        return entitiesToDTO(journey, journeyImgList, 0);
    }

    // 동행 게시글 삭제
    public void deleteJourney(Long id){

        try {

            Journey journey = journeyRepository.findById(id).get();
            journey.setStatus(Status.DELETED);

            journeyRepository.save(journey);
        }catch (Exception e){

            throw new RuntimeException("" + e.getMessage(), e);
        }
    }

    // 동행 게시글 수정
    public void updateJourney(JourneyUpdateRequestDto journeyUpdateRequestDto){

        try {

            // 동행 게시글 처리
            List<Object []> result = journeyRepository.readJourney(journeyUpdateRequestDto.getId());
            Member member = memberService.getMemberById(journeyUpdateRequestDto.getMemberId());

            Journey journey = Journey.builder()
                .id(journeyUpdateRequestDto.getId())
                .title(journeyUpdateRequestDto.getTitle())
                .content(journeyUpdateRequestDto.getContent())
                .city(journeyUpdateRequestDto.getCity())
                .startDate(journeyUpdateRequestDto.getStartDate())
                .endDate(journeyUpdateRequestDto.getEndDate())
                .count(journeyUpdateRequestDto.getCount())
                .longitude(journeyUpdateRequestDto.getLongitude())
                .latitude(journeyUpdateRequestDto.getLatitude())
                .member(member)
                .build();
            journey.setStatus(journeyUpdateRequestDto.getStatus());

            journeyRepository.save(journey);

            // 이미지 처리
            List<JourneyImgRequestDto> journeyImgRequestDtoList =  journeyUpdateRequestDto.getJourneyImgRequestDtoList();

            List<JourneyImg> journeyImgList = new ArrayList<>();

            result.forEach(arr -> {
                JourneyImg journeyImg = (JourneyImg) arr[1];
                journeyImgList.add(journeyImg);
            });

            for (int i=0; i<journeyImgRequestDtoList.size(); i++) {


                JourneyImgRequestDto journeyImgRequestDto = journeyImgRequestDtoList.get(i);

                // 사진을 기존보다 더 추가하게 될 때 구분.
                if (i < journeyImgList.size()) {

                    JourneyImg journeyImg = journeyImgList.get(i);

                    // 이미지 순서 혹은 위치가 다르다면
                    if (!journeyImg.getPath().equals(journeyImgRequestDto.getPath())){

                        // 이미지 삭제 처리
                        journeyImg.setStatus(Status.DELETED);
                        journeyImgList.set(i, journeyImg);

                        // 이미지 추가
                        journeyImg = JourneyImg.builder()
                            .path(journeyImgRequestDto.getPath())
                            .journey(journey)
                            .sequence(journeyImgRequestDto.getSequence())
                            .build();
                        journeyImg.setStatus(Status.ACTIVE);

                        journeyImgList.add(journeyImg);
                    }
                } else {
                    // 이미지 추가
                    JourneyImg journeyImg = JourneyImg.builder()
                        .path(journeyImgRequestDto.getPath())
                        .journey(journey)
                        .sequence(journeyImgRequestDto.getSequence())
                        .build();
                    journeyImg.setStatus(Status.ACTIVE);

                    journeyImgList.add(journeyImg);
                }

            }

            jourenyImgRepository.saveAll(journeyImgList);
        }catch (Exception e){

            throw new RuntimeException("" + e.getMessage(), e);
        }
    }

    public Journey findJourney(Long id){
        return journeyRepository.findById(id).get();
    }

    public List<JourneyResponseDto> myPageReadJourney(Long memberId){

        List<Object[]> result = journeyRepository.readMyPageJourney(memberId);

        List<JourneyResponseDto> journeyResponseDtoList = new ArrayList<>();

        for (Object[] arr : result){
            Journey journey = Journey.builder()
                .id((Long) arr[0])
                .title((String) arr[1])
                .city((String) arr[2])
                .status((Status) arr[3])
                .startDate((LocalDate) arr[4])
                .endDate((LocalDate) arr[5])
                .member((Member) arr[6])
                .build();

            JourneyImg journeyImg = (JourneyImg) arr[7];

            if (journeyImg == null) {
                // ProductImage가 없는 경우에 대한 처리
                journeyResponseDtoList.add(entitiesToDTO(journey, Collections.emptyList(), 0));
            } else {
                // ProductImage가 있는 경우에 대한 처리
                journeyResponseDtoList.add(entitiesToDTO(journey, Collections.singletonList(journeyImg), 0));
            }
        }

        return journeyResponseDtoList;
    }
}
