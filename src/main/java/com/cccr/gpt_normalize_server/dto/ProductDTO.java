package com.cccr.gpt_normalize_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private String platform;
    private String title;
    private String model;
    private String url;
    private String imageUrl;

    private double price;
    private String currency;
    private String condition;

    private int stock;
    private float rating;
    private int reviewCount;
}
