package com.diary.myDiary.domain.gpt.controller;

import com.diary.myDiary.domain.gpt.dto.AnalyzeEmotionRequest;
import com.diary.myDiary.domain.gpt.dto.ChatCompletionDTO;
import com.diary.myDiary.domain.gpt.dto.CompletionDTO;
import com.diary.myDiary.global.util.dto.ResponseVO;
import com.diary.myDiary.domain.gpt.service.ChatGPTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "감정분석 및 이미지", description = "GPT 관련 API")
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chatGpt")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    /**
     * ChatGPT 모델 리스트를 조회
     */
    @GetMapping("/modelList")
    public ResponseVO<List<Map<String, Object>>> selectModelList() {
        List<Map<String, Object>> result = chatGPTService.modelList();
        return new ResponseVO<>(result);
    }

    /**
     * ChatGPT 유효한 모델인지 조회
     */
    @GetMapping("/model")
    public ResponseVO<Map<String, Object>> isValidModel(@RequestParam(name = "modelName") String modelName) {
        Map<String, Object> result = chatGPTService.isValidModel(modelName);
        return new ResponseVO<>(result);
    }

    /**
     * Legacy ChatGPT 프롬프트 명령을 수행 :
     * gpt-3.5-turbo-instruct, babbage-002, davinci-002
     */
    @PostMapping("/legacyPrompt")
    public ResponseVO<Map<String, Object>> selectLegacyPrompt(@RequestBody CompletionDTO completionDto) {
        log.debug("param :: " + completionDto.toString());
        Map<String, Object> result = chatGPTService.legacyPrompt(completionDto);
        return new ResponseVO<>(result);
    }

    /**
     * 최신 ChatGPT 프롬프트 명령어를 수행 :
     * gpt-4, gpt-4 turbo, gpt-3.5-turbo
     */
    @PostMapping("/prompt")
    public ResponseVO<Map<String, Object>> selectPrompt(@RequestBody ChatCompletionDTO chatCompletionDto) {
        log.debug("param :: " + chatCompletionDto.toString());
        Map<String, Object> result = chatGPTService.prompt(chatCompletionDto);
        return new ResponseVO<>(result);
    }

    /**
     * 일기 내용을 기반으로 이미지 생성
     */
    @PostMapping("/generateImage")
    @Operation(summary = "이미지 생성", description = "일기 내용을 기반으로 이미지를 생성합니다.")
    public ResponseVO<Map<String, Object>> generateImage(@RequestBody String diaryContent, String emotionTag) {
        log.debug("Diary content :: " + diaryContent);
        log.debug("Emotion tag :: " + emotionTag);
        Map<String, Object> result = chatGPTService.generateImageFromDiary(diaryContent, emotionTag);
        return new ResponseVO<>(result);
    }

    /**
     * 일기 내용을 기반으로 감정 분석
     */
    @PostMapping("/analyzeEmotion")
    public Map<String, String> analyzeEmotion(@RequestBody AnalyzeEmotionRequest emotionRequest) {
        Long diaryId = emotionRequest.diaryId();
        return chatGPTService.analyzeEmotion(diaryId);
    }

//    /**
//     * 감정 분석이 완료되면 알림 생성
//     */
//    @PostMapping("/successNotification")
//    public ResponseVO<Map<String, Object>> successNotification(@RequestBody NotificationDTO notificationdto){
//
//        return null;
//    }
}