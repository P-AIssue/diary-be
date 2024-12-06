package com.diary.myDiary.domain.gpt.service.impl;

import com.diary.myDiary.domain.diary.entity.Diary;
import com.diary.myDiary.domain.diary.repository.DiaryRepository;
import com.diary.myDiary.domain.gpt.config.ChatGPTConfig;
import com.diary.myDiary.domain.gpt.dto.*;
import com.diary.myDiary.domain.gpt.service.ChatGPTService;
import com.diary.myDiary.domain.notification.repository.NotificationRepository;
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

        // 헤더 설정
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // 이미지 생성 API 엔드포인트
        String imageGenerationUrl = chatGPTConfig.getImageGenerationUrl();

        // 메시지 구성
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role", "user",
                "content", "다음 내용을 기반으로 : \"" + diaryContent + "\"\n파스텔 톤으로 그림 생성해줘"
        ));

        // 이미지 생성 요청 DTO 생성
        ImageGenerationRequestDTO requestDto = ImageGenerationRequestDTO.builder()
                .prompt(diaryContent)
                .n(1) // 생성할 이미지 수
                .size("1024x1024") // 이미지 크기
                .build();

        // 요청 엔티티 생성
        HttpEntity<ImageGenerationRequestDTO> requestEntity = new HttpEntity<>(requestDto, headers);

        // API 호출
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(imageGenerationUrl, HttpMethod.POST, requestEntity, String.class);

        Map<String, Object> resultMap = new HashMap<>();
        try {
            // 응답을 Map으로 변환
            resultMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.error("RuntimeException :: " + e.getMessage());
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> analyzeEmotion(Long diaryId) {

        Diary diary = diaryRepository.getByIdOrThrow(diaryId);
        String diaryContent = diary.getContent();
        Long memberId = diary.getMember().getId();


        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // 메시지 구성
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role", "user",
                "content", "다음 텍스트의 감정을 분석해 주세요: \"" + diaryContent + "\"\n감정 상태를 한 줄로 표현해 주세요."
        ));

        EmotionAnalyzeDTO emotionAnalyzeDto = EmotionAnalyzeDTO.builder()
                .messages(messages)
                .build();

        HttpEntity<EmotionAnalyzeDTO> requestEntity = new HttpEntity<>(emotionAnalyzeDto, headers);

        ResponseEntity<String> response = chatGPTConfig
                .restTemplate() 
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);

        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {});

            // 알림 생성
            String message = "감정분석이 완료되었습니다.";
            notificationService.sendNotification(memberId, message);

        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException :: " + e.getMessage());
        }
        return resultMap; // 분석 결과 반환
    }

//    @Override
//    public Map<String, Object> successNotification(Long notificationId) {
//        log.debug("[+] 성공 알림을 생성합니다. 알림 ID: {}", notificationId);
//
//        // 헤더 설정
//        HttpHeaders headers = chatGPTConfig.httpHeaders();
//
//        // 알림 데이터 생성
//        NotificationDTO notificationDto = NotificationDTO.builder()
//                .notificationId(notificationId)
//                .message("감정 분석이 완료되었습니다.")
//                .build();
//
//        // 요청 엔티티 생성
//        HttpEntity<NotificationDTO> requestEntity = new HttpEntity<>(notificationDto, headers);
//
//        // API 호출
//        String notificationUrl = chatGPTConfig
//                .restTemplate()
//                .exchange(notificationUrl, HttpMethod.POST, requestEntity, String.class);// 실제 알림 API URL
//
//        ResponseEntity<Map> response;
//        try {
//            response = chatGPTConfig.restTemplate()
//                    .postForEntity(notificationUrl, requestEntity, Map.class);
//
//            // 성공 응답 처리
//            if (response.getStatusCode().is2xxSuccessful()) {
//                log.info("[+] 알림 전송 성공: {}", response.getBody());
//                return response.getBody();
//            } else {
//                throw new RuntimeException("알림 전송 실패: " + response.getStatusCode());
//            }
//        } catch (Exception e) {
//            log.error("알림 전송 중 예외 발생: {}", e.getMessage());
//            throw new RuntimeException("알림 전송 중 오류가 발생했습니다.", e);
//        }
//    }


}