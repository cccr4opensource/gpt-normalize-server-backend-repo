package com.cccr.gpt_normalize_server.service;

import com.cccr.gpt_normalize_server.dto.ProductDTO;
import com.cccr.gpt_normalize_server.entity.Product;
import com.cccr.gpt_normalize_server.mapper.ProductMapper;
import com.cccr.gpt_normalize_server.dto.NormalizeRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final ProductMapper productMapper;

    @Value("${openai.api-key}")
    private String openaiApiKey;

    public Product normalizeAndConvert(NormalizeRequestDTO requestDTO) {
        try {
            // 추가된 로그 : requestDTO.getRawJson()의 실제 값을 확인한다.
            log.info("📋 requestDTO.getRawJson():\n{}", requestDTO.getRawJson());

            // 추가된 null/empty 로그
            if (requestDTO.getRawJson() == null || requestDTO.getRawJson().isEmpty()) {
                log.error("❌ requestDTO.getRawJson()이 null 또는 비어있습니다.");
                return null;
            }


            // NormalizeRequestDTO의 rawJson 필드가 이제 String이므로,
            // 이를 JsonNode로 변환하는 과정이 추가됩니다.

            String prettyJson = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(requestDTO.getRawJson()); // 변환된 JsonNode를 사용합니다.
            log.info("📋 prettyJson (Input to GPT):\n{}", requestDTO.getRawJson()); // GPT에 전달될 prettyJson 확인용 로그

            String userPrompt = String.format("""
                주어진 원시 JSON 데이터에서 정보를 **추출하여** 다음 JSON 형식에 맞춰 정규화된 JSON 객체를 생성해줘.

                **출력 JSON 형식 (Key는 절대 바꾸지 마세요. 숫자 타입은 0 또는 0.0으로 예시):**
                {
                  "platform": "%s",
                  "title": "...",
                  "price": 0.0,
                  "currency": "...",
                  "condition": "...",
                  "url": "...",
                  "imageUrl": "...",
                  "model": "...",
                  "stock": 0,
                  "rating": 0.0,
                  "reviewCount": 0
                }

                **필드별 추출 및 변환 지침:**
                - `platform`: "%s" 값을 사용하세요.
                - `title`: 원본 JSON의 'title' 값을 그대로 사용하세요.
                - `price`: 원본 JSON의 'price' 값에서 '$'와 ',' 기호를 제거하고 숫자(double)로 변환하세요.
                - `currency`: 원본 JSON의 'price'에 '$' 기호가 있다면 'USD'로 설정하고, '원'이 있다면 'KRW'로 설정하세요.
                - `condition`: 원본 JSON의 'condition' 값을 그대로 사용하세요.
                - `url`: 원본 JSON에 'link'라는 키가 있다면 그 값을 사용하고, 없다면 'url' 키 값을 사용하세요.
                - `imageUrl`: 원본 JSON에 'imageUrl' 또는 'image' 키가 있다면 그 값을 사용하세요.
                - `model`: 원본 JSON에 'model' 키가 있다면 그 값을 사용하세요.
                - `stock`: 원본 JSON에 'stock' 키가 있다면 숫자(int)로 변환하여 사용하세요.
                - `rating`: 원본 JSON에 'rating' 키가 있다면 숫자(float)로 변환하여 사용하세요.
                - `reviewCount`: 원본 JSON에 'reviewCount' 또는 'reviews' 키가 있다면 숫자(int)로 변환하여 사용하세요.

                **중요:** 모든 필드는 반드시 포함되어야 합니다. 원본 JSON에서 값을 찾을 수 없거나 유추하기 어렵다면 그때만 `null`로 채워주세요.

                ---
                **예시:**

                **원본 JSON 입력:**
                ```json
                {
                  "title": "Dell R730xd Server",
                  "price": "$499",
                  "stock": 3,
                  "condition": "Used",
                  "link": "[https://ebay.com/view?id=123](https://ebay.com/view?id=123)"
                }
                ```

                **기대하는 정규화된 JSON 출력:**
                ```json
                {
                  "platform": "%s",
                  "title": "Dell R730xd Server",
                  "price": 499.0,
                  "currency": "USD",
                  "condition": "Used",
                  "url": "[https://ebay.com/view?id=123](https://ebay.com/view?id=123)",
                  "imageUrl": null,
                  "model": null,
                  "stock": 3,
                  "rating": null,
                  "reviewCount": null
                }
                ```
                ---

                **이제 다음 원본 JSON을 정규화해줘:**
                ```json
                %s
                ```
                """, requestDTO.getPlatform(), requestDTO.getPlatform(), requestDTO.getPlatform(), prettyJson);

            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-4o",
                    "messages", List.of(
                            Map.of("role", "system", "content", "너는 사용자로부터 원시 JSON 데이터를 받아 지정된 형식으로 정규화하고 필요한 경우 값을 추출하거나 변환하는 전문 AI 어시스턴트야. 항상 유효한 JSON 객체만 응답해야 해."),
                            Map.of("role", "user", "content", userPrompt)
                    ),
                    "temperature", 0.2,
                    "response_format", Map.of("type", "json_object")
            );

            String gptResponse = webClient.post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + openaiApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (gptResponse == null) {
                log.error("❌ GPT 응답이 null입니다.");
                return null;
            }

            JsonNode root = objectMapper.readTree(gptResponse);
            if (root.has("error")) {
                log.error("❌ GPT 에러 응답:\n{}", gptResponse);
                return null;
            }

            String content = root.path("choices").get(0)
                    .path("message")
                    .path("content")
                    .asText()
                    .replaceAll("(?s)```json\\s*", "")
                    .replaceAll("(?s)```", "")
                    .trim();

            log.info("✅ GPT가 정규화하여 반환한 원본 JSON 문자열:\n{}", content);


            if (content.isEmpty()) {
                log.error("❌ GPT 응답에서 content가 비어있습니다.");
                return null;
            }

            ProductDTO dto = objectMapper.readValue(content, ProductDTO.class);
            Product entity = productMapper.toEntity(dto);

            log.info("✅ 정규화된 Product: {}", entity);
            return entity;

        } catch (Exception e) {
            log.error("❌ GPT 응답 처리 실패: {}", e.getMessage(), e);
            return null;
        }
    }
}