package com.cccr.gpt_normalize_server.dto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;


// 정규화 요청 DTO
@Data
public class NormalizeRequestDTO {
    private String platform;
    private JsonNode rawJson;
}
