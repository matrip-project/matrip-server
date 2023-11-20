package com.v1.matripserver.journey.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        Member member = memberService.getMember(journeyRequestDto.getMemberId());

        // 메서드로 수정
        Journey journey = Journey.builder()
            .title(journeyRequestDto.getTitle())
            .content(journeyRequestDto.getContent())
            .member(member).build();

        List<JourneyImg> journeyImgList = new ArrayList<>();

        for (String path : journeyRequestDto.getImagePathList()){

            // 메서드로 수정
            JourneyImg journeyImg = JourneyImg.builder()
                .path(path)
                .journey(journey)
                .build();
            
            journeyImgList.add(journeyImg);
        }

        journeyRepository.save(journey);
        if (!journeyImgList.isEmpty()){
            jourenyImgRepository.saveAll(journeyImgList);
        }
    }

    // 동행 게시글 조회
    public PageResponseDTO<JourneyResponseDto, Object[]> readJourney(PageRequestDTO pageRequestDTO){
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("id").descending());

        Page<Object []> result = journeyRepository.readJourney(pageable, pageRequestDTO.getKeyword());

        Function<Object [], JourneyResponseDto> fn = (arr -> {
            Journey journey = (Journey) arr[0];
            JourneyImg journeyImg = (JourneyImg) arr[1];

            if (journeyImg == null) {
                // ProductImage가 없는 경우에 대한 처리
                return entitiesToDTO(journey, Collections.emptyList());
            } else {
                // ProductImage가 있는 경우에 대한 처리
                return entitiesToDTO(journey, Collections.singletonList(journeyImg));
            }
        });

        return new PageResponseDTO<>(result, fn);
    }

    // 검색 결과 반환
    JourneyResponseDto entitiesToDTO(Journey journey, List<JourneyImg> journeyImgList){

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
            .journeyImgRequestDtoList(journeyImgRequestDtoList)
            .created(journey.getCreated())
            .updated(journey.getUpdated())
            .mid(journey.getMemberId().getId())
            .mName(journey.getMemberId().getName())
            .build();
    }
}