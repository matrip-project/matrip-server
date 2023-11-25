package com.v1.matripserver.journey.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.v1.matripserver.journey.dto.JourneyImgRequestDto;
import com.v1.matripserver.journey.dto.JourneyRequestDto;
import com.v1.matripserver.journey.dto.JourneyResponseDto;
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

        // 메서드로 수정
        Journey journey = Journey.builder()
            .title(journeyRequestDto.getTitle())
            .content(journeyRequestDto.getContent())
            .city(journeyRequestDto.getCity())
            .count(journeyRequestDto.getCount())
            .schedule(journeyRequestDto.getSchedule())
            .latitude(journeyRequestDto.getLatitude())
            .longitude(journeyRequestDto.getLongitude())
            .member(member)
            .build();

        journey.setStatus(Status.ACTIVE);

        List<JourneyImg> journeyImgList = new ArrayList<>();

        for (String path : journeyRequestDto.getImagePathList()){

            // 메서드로 수정
            JourneyImg journeyImg = JourneyImg.builder()
                .path(path)
                .journey(journey)
                .build();
            journeyImg.setStatus(Status.ACTIVE);
            journeyImgList.add(journeyImg);
        }

        journeyRepository.save(journey);
        if (!journeyImgList.isEmpty()){
            jourenyImgRepository.saveAll(journeyImgList);
        }
    }

    // 동행 게시글 조회
    public PageResponseDTO<JourneyResponseDto, Object[]> readJourneyList(PageRequestDTO pageRequestDTO){
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("id").descending());

        Page<Object []> result = journeyRepository.readJourneyList(pageable, pageRequestDTO.getKeyword());

        Function<Object [], JourneyResponseDto> fn = (arr -> {
            Journey journey = (Journey) arr[0];
            JourneyImg journeyImg = (JourneyImg) arr[1];
            Integer count = ((Long) arr[2]).intValue();

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
                    .path(journeyImg.getPath())
                    .build();
            }).toList();

        return JourneyResponseDto.builder()
            .id(journey.getId())
            .title(journey.getTitle())
            .content(journey.getContent())
            .count(journey.getCount())
            .schedule(journey.getSchedule())
            .latitude(journey.getLatitude())
            .longitude(journey.getLongitude())
            .journeyImgRequestDtoList(journeyImgRequestDtoList)
            .created(journey.getCreated())
            .updated(journey.getUpdated())
            .journeyCount(count)
            .mid(journey.getMemberId().getId())
            .mName(journey.getMemberId().getName())
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
    public void updateJourney(JourneyRequestDto journeyRequestDto){

        try {

            Journey journey = journeyRepository.findById(journeyRequestDto.getMemberId()).get();
            if (!journeyRequestDto.getTitle().isEmpty()){
                journey.setTitle(journeyRequestDto.getTitle());
            }
            if (!journeyRequestDto.getContent().isEmpty()){
                journey.setContent(journeyRequestDto.getContent());
            }

            journeyRepository.save(journey);
        }catch (Exception e){

            throw new RuntimeException("" + e.getMessage(), e);
        }
    }
}
