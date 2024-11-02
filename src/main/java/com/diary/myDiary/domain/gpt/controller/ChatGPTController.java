package com.diary.myDiary.domain.gpt.controller;


import com.diary.myDiary.domain.gpt.dto.ChatCompletionDTO;
import com.diary.myDiary.domain.gpt.dto.CompletionDTO;
import com.diary.myDiary.domain.gpt.dto.ResponseVO;
import com.diary.myDiary.domain.gpt.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chatGpt")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    /**
     * [API] ChatGPT 모델 리스트를 조회
     */
    @GetMapping("/modelList")
    public ResponseVO<List<Map<String, Object>>> selectModelList() {
        List<Map<String, Object>> result = chatGPTService.modelList();
        return new ResponseVO<>(result);
    }

    /**
     * [API] ChatGPT 유효한 모델인지 조회
     */
    @GetMapping("/model")
    public ResponseVO<Map<String, Object>> isValidModel(@RequestParam(name = "modelName") String modelName) {
        Map<String, Object> result = chatGPTService.isValidModel(modelName);
        return new ResponseVO<>(result);
    }

    /**
     * [API] Legacy ChatGPT 프롬프트 명령을 수행 :
     * gpt-3.5-turbo-instruct, babbage-002, davinci-002
     */
    @PostMapping("/legacyPrompt")
    public ResponseVO<Map<String, Object>> selectLegacyPrompt(@RequestBody CompletionDTO completionDto) {
        log.debug("param :: " + completionDto.toString());
        Map<String, Object> result = chatGPTService.legacyPrompt(completionDto);
        return new ResponseVO<>(result);
    }

    /**
     * [API] 최신 ChatGPT 프롬프트 명령어를 수행 :
     * gpt-4, gpt-4 turbo, gpt-3.5-turbo
     */
    @PostMapping("/prompt")
    public ResponseVO<Map<String, Object>> selectPrompt(@RequestBody ChatCompletionDTO chatCompletionDto) {
        log.debug("param :: " + chatCompletionDto.toString());
        Map<String, Object> result = chatGPTService.prompt(chatCompletionDto);
        return new ResponseVO<>(result);
    }
}