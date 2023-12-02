package com.v1.matripserver.journey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.v1.matripserver.journey.dto.JourneyRequestDto;
import com.v1.matripserver.journey.dto.JourneyResponseDto;
import com.v1.matripserver.journey.dto.JourneyUpdateRequestDto;
import com.v1.matripserver.journey.dto.PageRequestDTO;
import com.v1.matripserver.journey.dto.PageResponseDTO;
import com.v1.matripserver.journey.service.JourneyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/journeys")
public class JourneyController {

    private final JourneyService journeyService;

    // 동행 게시글 작성
    @PostMapping("")
    public ResponseEntity<?> createJourney(@RequestBody JourneyRequestDto journeyRequestDto){

        journeyService.createJourney(journeyRequestDto);
        return ResponseEntity.ok().build();
    }

    // 동행 게시글 조회
    @GetMapping("")
    public ResponseEntity<?> readJourneyList(PageRequestDTO pageRequestDTO){

        PageResponseDTO<JourneyResponseDto, Object[]> pageResponseDTO = journeyService.readJourneyList(pageRequestDTO);
        return ResponseEntity.ok(pageResponseDTO);
    }

    // 동행 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<JourneyResponseDto> readJourney(@PathVariable Long id){

        JourneyResponseDto journeyResponseDto = journeyService.readJourney(id);
        return ResponseEntity.ok(journeyResponseDto);
    }

    // 동행 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJourney(@PathVariable Long id) {
        journeyService.deleteJourney(id);
        return ResponseEntity.ok().build();
    }

    // 동행 게시글 수정
    @PutMapping("")
    public ResponseEntity<?> updateJourney(@RequestBody JourneyUpdateRequestDto journeyUpdateRequestDto) {

        journeyService.updateJourney(journeyUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> myPageReadJourney(Long memberId) {

        List<JourneyResponseDto> journeyResponseDtoList = journeyService.myPageReadJourney(memberId);
        return ResponseEntity.ok(journeyResponseDtoList);
    }
}