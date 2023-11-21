package com.v1.matripserver.journey.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.v1.matripserver.journey.dto.JourneyRequestDto;
import com.v1.matripserver.journey.dto.JourneyResponseDto;
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
    public ResponseEntity createJourney(@RequestBody JourneyRequestDto journeyRequestDto){

        try {
            journeyService.createJourney(journeyRequestDto);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            log.error("" + e.getMessage(), e);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    // 동행 게시글 조회
    @GetMapping("")
    public ResponseEntity<PageResponseDTO> readJourney(PageRequestDTO pageRequestDTO){
        try {
            PageResponseDTO<JourneyResponseDto, Object[]> pageResponseDTO = journeyService.readJourney(pageRequestDTO);
            return new ResponseEntity<>(pageResponseDTO, HttpStatus.OK);
        }catch (Exception e){
            log.error("" + e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 동행 게시글 삭제
    @DeleteMapping("")
    public ResponseEntity deleteJourney(JourneyRequestDto journeyRequestDto) {
        try {
            journeyService.deleteJourney(journeyRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.error("" + e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
