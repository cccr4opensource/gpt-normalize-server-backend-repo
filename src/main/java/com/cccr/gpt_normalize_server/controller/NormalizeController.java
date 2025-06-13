package com.cccr.gpt_normalize_server.controller;

import com.cccr.gpt_normalize_server.dto.NormalizeRequestDTO;
import com.cccr.gpt_normalize_server.dto.ProductDTO;
import com.cccr.gpt_normalize_server.entity.Product;
import com.cccr.gpt_normalize_server.service.GptService;
import com.cccr.gpt_normalize_server.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/normalize")
@RequiredArgsConstructor
@Slf4j
public class NormalizeController {

    private final ProductService productService;
    private final GptService gptService;

    // DTO -> ENTITY 객체로 저장
    // 컨트롤러에서 DTO 대신에 RAW JSON 받기
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void normalizeAndSave(@RequestBody NormalizeRequestDTO requestDTO) {

        // 컨트롤러에 들어온 requestDTO 전체를 확인합니다.
        log.info("normalizeController received requestDTO: {}", requestDTO);

        Product product = gptService.normalizeAndConvert(requestDTO);
        if (product != null) {
            productService.save(product);
            log.info("✅ 정규화 후 저장 완료: {}", product.getTitle());
        } else {
            log.warn("❌ 정규화 실패 또는 파싱 오류 발생");
        }
    }
}
