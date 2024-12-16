package com.diary.myDiary.domain.gpt.service.impl;

import com.diary.myDiary.domain.diary.entity.Diary;
import com.diary.myDiary.domain.diary.repository.DiaryRepository;
import com.diary.myDiary.domain.gpt.config.ChatGPTConfig;
import com.diary.myDiary.domain.gpt.dto.*;
import com.diary.myDiary.domain.gpt.service.ChatGPTService;
import com.diary.myDiary.domain.notification.repository.NotificationRepository;
import com.diary.myDiary.global.util.AESUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.diary.myDiary.domain.notification.service.NotificationService;

/**
 * ChatGPT Service 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTServiceImpl implements ChatGPTService {

    private final DiaryRepository diaryRepository;
    private final ChatGPTConfig chatGPTConfig;
    private final ObjectMapper objectMapper;

    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @Value("${openai.url.model}")
    private String modelUrl;

    @Value("${openai.url.model-list}")
    private String modelListUrl;

    @Value("${openai.url.prompt}")
    private String promptUrl;

    @Value("${openai.url.legacy-prompt}")
    private String legacyPromptUrl;

    /**
     * 사용 가능한 모델 리스트를 조회하는 비즈니스 로직
     */
    @Override
    public List<Map<String, Object>> modelList() {
        log.debug("[+] 모델 리스트를 조회합니다.");
        List<Map<String, Object>> resultList = null;

        // 토큰 정보가 포함된 Header를 가져온다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // 통신을 위한 RestTemplate을 구성
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(modelUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        try {
            // Jackson을 기반으로 응답
            Map<String, Object> data = objectMapper.readValue(response.getBody(), new TypeReference<>() {
            });

            // 응답 값을 결과값에 넣고 출력
            resultList = (List<Map<String, Object>>) data.get("data");
            for (Map<String, Object> object : resultList) {
                log.debug("ID: " + object.get("id"));
                log.debug("Object: " + object.get("object"));
                log.debug("Created: " + object.get("created"));
                log.debug("Owned By: " + object.get("owned_by"));
            }
        } catch (JsonMappingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (JsonProcessingException e) {
            log.debug("JsonProcessingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }
        return resultList;
    }

    /**
     * 모델이 유효한지 확인하는 비즈니스 로직
     */
    @Override
    public Map<String, Object> isValidModel(String modelName) {
        log.debug("[+] 모델이 유효한지 조회합니다. 모델 : " + modelName);
        Map<String, Object> result = new HashMap<>();

        // 토큰 정보가 포함된 Header를 가져온다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // 통신을 위한 RestTemplate을 구성
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(modelListUrl + "/" + modelName, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        try {
            // Jackson을 기반으로 응답값을 가져온다.
            result = objectMapper.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }
        return result;
    }

    /**
     * ChatGTP 프롬프트 검색
     */
    @Override
    public Map<String, Object> legacyPrompt(CompletionDTO completionDto) {
        log.debug("[+] 레거시 프롬프트를 수행합니다.");

        // 토큰 정보가 포함된 Header를 가져온다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // 통신을 위한 RestTemplate을 구성
        HttpEntity<CompletionDTO> requestEntity = new HttpEntity<>(completionDto, headers);
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(legacyPromptUrl, HttpMethod.POST, requestEntity, String.class);

        Map<String, Object> resultMap = new HashMap<>();
        try {
            // String -> HashMap 역직렬화를 구성
            resultMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }
        return resultMap;
    }

    /**
     * 최신 모델에 대한 프롬프트
     */
    @Override
    public Map<String, Object> prompt(ChatCompletionDTO chatCompletionDto) {
        log.debug("[+] 신규 프롬프트를 수행합니다.");

        Map<String, Object> resultMap = new HashMap<>();

        // 토큰 정보가 포함된 Header를 가져온다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // 통신을 위한 RestTemplate을 구성
        HttpEntity<ChatCompletionDTO> requestEntity = new HttpEntity<>(chatCompletionDto, headers);
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);
        try {
            // String -> HashMap 역직렬화를 구성
            resultMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> generateImageFromDiary(String diaryContent) {
        log.debug("[+] 일기 내용을 기반으로 이미지를 생성합니다.");

        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // 최신 이미지 생성 API 엔드포인트 (예: DALL·E 3)
        String latestImageGenerationUrl = chatGPTConfig.getImageGenerationUrl();

        // 프롬프트 개선
        // 목표: 보는 사람으로 하여금 즉각적인 미소와 즐거움을 주는 고품질의 아트웍
        // 스튜디오 지브리 풍의 따뜻한 애니메이션 스타일 강조
        // 아기자기한 디테일 포함: 작은 마을 풍경, 예쁜 꽃밭, 나비, 환하게 빛나는 하늘
        // 색감: 밝은 파스텔 그라데이션 + 따뜻한 주황빛 섞기
        // 전반적인 느낌: 동화책 삽화 같고, 애니메이션 포스터처럼 깔끔한 마감
        String improvedPrompt =
                "당신은 뛰어난 일러스트레이터로서, 아래 일기 내용을 바탕으로 " +
                        "보는 사람 모두를 미소 짓게 할 정도로 사랑스럽고 아기자기한 애니메이션풍 일러스트를 그려주세요.\n\n" +
                        "스타일 가이드:\n" +
                        "1. 스튜디오 지브리 풍: 부드럽고 따뜻한 색감, 따사로운 햇살이 어린 듯한 분위기.\n" +
                        "2. 장면: 평화로운 작은 마을 풍경. 작은 꽃밭, 알록달록한 꽃들 사이로 나비가 날아다니고, " +
                        "작은 버섯집들이 여기저기 흩어져 있으며, 부드러운 곡선으로 표현된 나무들이 서 있는 모습.\n" +
                        "3. 색감: 밝은 파스텔 그라데이션을 메인으로 하되, 따뜻한 주황빛, 연한 연두색, 하늘색을 섞어 " +
                        "전체적으로 화사하고 기분 좋은 분위기.\n" +
                        "4. 질감: 수채화 느낌으로 부드럽고 자연스럽게 번진 색감, 디지털 일러스트지만 " +
                        "붓터치가 느껴지는 듯한 자연스러운 마감.\n" +
                        "5. 전체적인 느낌: 동화책 삽화처럼 아기자기하고 따뜻한 장면. " +
                        "사진처럼 사실적이지 않고, 명확히 그림으로 인식될 수 있는 깔끔한 마감.\n\n" +
                        "참고 일기 내용: \"" + diaryContent + "\"\n" +
                        "이 장면을 자유롭게 해석하고 확장하여, " +
                        "일기에서 느껴지는 감정과 분위기를 이 아름다운 일러스트 장면에 녹여주세요.";

        ImageGenerationRequestDTO requestDto = ImageGenerationRequestDTO.builder()
                .model("dall-e-3")
                .prompt(improvedPrompt)
                .n(1)
                .size("1024x1024")
                .build();

        HttpEntity<ImageGenerationRequestDTO> requestEntity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(latestImageGenerationUrl, HttpMethod.POST, requestEntity, String.class);

        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.error("RuntimeException :: " + e.getMessage());
        }

        return resultMap;
    }


    @Override
    public Map<String, String> analyzeEmotion(Long diaryId) {
        Diary diary = diaryRepository.getByIdOrThrow(diaryId);
        String encryptedContent = diary.getContent();
        Long memberId = diary.getMember().getId();

        String decryptedContent;
        try {
            decryptedContent = AESUtil.decrypt(encryptedContent);
        } catch (Exception e) {
            log.error("일기 내용 복호화 실패", e);
            throw new RuntimeException("복호화 중 오류 발생");
        }

        HttpHeaders headers = chatGPTConfig.httpHeaders();

        List<Map<String, String>> messages = List.of(
                Map.of(
                        "role", "user",
                        "content", "아래의 텍스트를 읽고 그 내포된 감정을 분석해 주세요. " +
                                "감정은 행복, 사랑, 감사, 안도, 무난, 슬픔, 실망, 피곤, 화남 중 하나나 복합적으로 나타날 수 있습니다. " +
                                "전반적인 감정 상태를 핵심 단어와 짧은 문장으로 명확하게 표현해 주시기 바랍니다.\n\n" +
                                "분석 대상 텍스트: \"" + decryptedContent + "\""
                )
        );

        // 요청 DTO
        EmotionAnalyzeDTO emotionAnalyzeDto = new EmotionAnalyzeDTO("gpt-4", messages);

        HttpEntity<EmotionAnalyzeDTO> requestEntity = new HttpEntity<>(emotionAnalyzeDto, headers);

        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);

        try {
            Map<String, Object> resultMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
            List<Map<String, Object>> choices = (List<Map<String, Object>>) resultMap.get("choices");

            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> firstChoice = choices.get(0);
                Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                String content = (String) message.get("content");

                String notificationMessage = "감정분석이 완료되었습니다.";
                notificationService.sendNotification(memberId, notificationMessage);

                // content만 담은 Map 반환
                return Map.of("content", content.trim());
            }

        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException :: " + e.getMessage());
        }

        // 실패 시 빈 content 반환
        return Map.of("content", "");
    }

}