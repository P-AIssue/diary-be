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
    public Map<String, Object> generateImageFromDiary(String diaryContent, String emotionTag) {
        log.debug("[+] 일기 내용을 기반으로 이미지를 생성합니다.");

        HttpHeaders headers = chatGPTConfig.httpHeaders();
        String latestImageGenerationUrl = chatGPTConfig.getImageGenerationUrl();

        String improvedPrompt =
                "당신은 감정을 시각적으로 표현하는 뛰어난 일러스트레이터입니다. 아래 일기 내용과 감정 상태를 바탕으로, " +
                        "보는 사람에게 해당 감정을 생생하게 전달하는 애니메이션풍 일러스트를 그려주세요.\n\n" +
                        "감정 상태: \"" + emotionTag + "\"\n" +
                        "이 감정 상태를 반영하여, 장면의 분위기, 색감, 디테일, 그리고 구성 요소를 조정해 주세요.\n\n" +
                        "감정별 가이드:\n" +
                        "- 행복: 따뜻한 햇살이 가득한 들판, 활짝 핀 밝은 노란색과 주황색 꽃들, " +
                        "활기차게 날아다니는 나비들, 맑고 파란 하늘과 푸른 언덕.\n" +
                        "- 슬픔: 조용한 숲 속의 작은 오두막, 부드러운 파스텔 블루와 연보라 색감, " +
                        "약간 흐린 하늘과 물방울이 맺힌 잎사귀, 위로를 주는 따뜻한 빛 한 줄기.\n" +
                        "- 사랑: 로맨틱한 분홍빛 하늘과 붉은 장미 정원, 서로 마주 보고 있는 두 나비, " +
                        "부드러운 파스텔 톤과 황금빛 석양의 따스함.\n" +
                        "- 피곤: 조용히 쉬고 있는 숲 속 벤치, 어스름한 저녁의 연한 회색과 녹색 배경, " +
                        "편안함을 주는 따뜻한 조명과 부드러운 색감의 구도.\n" +
                        "- 화남: 격렬한 폭풍이 몰아치는 바다, 어두운 붉은빛과 검푸른 물결, " +
                        "강렬한 번개와 흔들리는 나무들로 표현되는 격정적인 분위기.\n" +
                        "- 안도: 평온한 호수와 그 위를 떠다니는 오리, 잔잔한 물결과 부드러운 구름, " +
                        "따뜻한 베이지와 연녹색의 색감으로 표현된 평화로운 장면.\n\n" +
                        "스타일 가이드:\n" +
                        "1. 스튜디오 지브리 풍: 부드럽고 따뜻한 색감, 감정 상태를 자연스럽게 녹여낸 분위기.\n" +
                        "2. 장면: 감정 상태와 일기 내용을 반영한 맞춤 디테일 (예: 슬픔 -> 빗방울 맺힌 창문, 행복 -> 활짝 핀 꽃밭 등).\n" +
                        "3. 색감: 감정 상태에 맞는 톤을 강조하고, 전체적으로 자연스럽고 조화롭게 구성.\n" +
                        "4. 질감: 수채화 느낌으로 부드럽고 자연스럽게 번진 색감, 디지털 일러스트에서 따뜻한 마감.\n" +
                        "5. 전체적인 느낌: 동화책 삽화처럼 아기자기하고 감정을 잘 전달할 수 있는 장면 구성.\n\n" +
                        "참고 일기 내용: \"" + diaryContent + "\"\n" +
                        "이 감정 상태('" + emotionTag + "')를 최대한 사실적으로 반영하고, " +
                        "일기에서 느껴지는 감정과 분위기를 이 장면에 담아주세요.";

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

        String prompt = "아래의 텍스트를 바탕으로 감정을 분석한 뒤, 해당 감정 상태에 어울리는 간단하고 실용적인 활동이나 콘텐츠를 추천해 주세요.\n\n" +
                "감정 범주: 행복, 사랑, 감사, 안도, 무난, 슬픔, 실망, 피곤, 화남 등 복합적 가능.\n" +
                "1단계: 텍스트에 담긴 감정을 파악하고, 핵심 단어를 정리한 뒤 1~2문장 정도로 감정 상태를 요약.\n" +
                "2단계: 해당 감정 상태에 어울리는 행동/콘텐츠(예: 짧은 운동, 음악 추천, 친구와 연락, 힐링되는 동영상, 명상 앱 추천 등)를 2~3가지 제안.\n\n" +
                "응답 형식:\n" +
                "감정 분석:\n[분석결과]\n\n" +
                "추천 콘텐츠:\n[콘텐츠 제안]\n\n" +
                "분석 대상 텍스트: \"" + decryptedContent + "\"";

        List<Map<String, String>> messages = List.of(
                Map.of(
                        "role", "user",
                        "content", prompt
                )
        );

        EmotionAnalyzeDTO emotionAnalyzeDto = new EmotionAnalyzeDTO("gpt-4", messages);

        HttpEntity<EmotionAnalyzeDTO> requestEntity = new HttpEntity<>(emotionAnalyzeDto, headers);

        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);

        String analysis = "";
        String recommendations = "";

        try {
            Map<String, Object> resultMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
            List<Map<String, Object>> choices = (List<Map<String, Object>>) resultMap.get("choices");

            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> firstChoice = choices.get(0);
                Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                String content = (String) message.get("content");

                // 감정 분석과 추천 콘텐츠 추출 로직
                int analysisIndex = content.indexOf("감정 분석:");
                int recommendIndex = content.indexOf("추천 콘텐츠:");

                if (analysisIndex != -1 && recommendIndex != -1) {
                    analysis = content.substring(analysisIndex + "감정 분석:".length(), recommendIndex).trim();
                    recommendations = content.substring(recommendIndex + "추천 콘텐츠:".length()).trim();
                } else {
                    // 포맷이 예상과 다르면 전체 content를 분석으로 처리하거나 빈값 처리
                    analysis = content.trim();
                    recommendations = "";
                }

                String notificationMessage = "감정분석 및 추천 콘텐츠 제안이 완료되었습니다.";
                notificationService.sendNotification(memberId, notificationMessage);
            }

        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException :: " + e.getMessage());
        }

        return Map.of(
                "analysis", analysis,
                "recommendations", recommendations
        );
    }
}