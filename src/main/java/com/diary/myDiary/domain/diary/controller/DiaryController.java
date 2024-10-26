package com.diary.myDiary.domain.diary.controller;

import com.diary.myDiary.domain.diary.dto.DiaryRequest;
import com.diary.myDiary.domain.diary.dto.DiaryResponse;
import com.diary.myDiary.domain.diary.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 일기작성
     */
    @PostMapping
    @Operation(summary = "일기 작성", description = "일기를 작성합니다.")
    public ResponseEntity<DiaryResponse> writeDiary(@Valid @RequestBody DiaryRequest diaryRequest) {
        return ResponseEntity.ok(diaryService.create(diaryRequest.content(), diaryRequest.emotionTag()));
    }

    /**
     * 일기삭제
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "일기 삭제", description = "일기를 삭제합니다.")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDiary(@PathVariable Long id) {
        diaryService.remove(id);
    }

    /**
     *  일기조회
     */
    @GetMapping("/{id}")
    @Operation(summary = "일기 조회", description = "일기를 조회합니다.")
    public ResponseEntity<DiaryResponse> getDiary(@PathVariable Long id) {
        return ResponseEntity.ok(diaryService.getDiary(id));
    }

    /**
     * 일기목록조회
     */
    @GetMapping
    @Operation(summary = "일기 목록 조회", description = "일기 목록을 조회합니다.")
    public ResponseEntity<?> getDiaryList(Pageable pageable) {
        return ResponseEntity.ok(diaryService.getDiaryList(pageable));
    }
}
