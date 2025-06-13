package com.cccr.gpt_normalize_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


// 정규화 응답에 대한 DTO
@Data
@AllArgsConstructor
public class NormalizeResponseDTO {
    private boolean success;
    private String message;
}
