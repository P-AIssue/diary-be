package com.diary.myDiary.domain.diary.controller;

import com.diary.myDiary.domain.diary.dto.WriteDTO;
import com.diary.myDiary.domain.diary.entity.Diary;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/diary")
public class DiaryController {

//    private final DiaryService diaryService;

    /**
     * 일기작성
     */
    @PostMapping("/write")
    @Operation(summary = "일기 작성", description = "일기를 작성합니다.")
    @ResponseStatus(HttpStatus.OK)
    public void writeDiary(@Valid @RequestBody WriteDTO writeDTO) throws Exception {

    }

    /**
     * 일기삭제
     */
    @DeleteMapping("/delete")
    @Operation(summary = "일기 삭제", description = "일기를 삭제합니다.")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDiary() throws Exception {
    }

    /**
     *  일기조회
     */
    @GetMapping("/{id}")
    @Operation(summary = "일기 조회", description = "일기를 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    public void getDiary() throws Exception {}
}
