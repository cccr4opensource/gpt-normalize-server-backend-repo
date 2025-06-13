package com.cccr.gpt_normalize_server.mapper;

import com.cccr.gpt_normalize_server.dto.ProductDTO;
import com.cccr.gpt_normalize_server.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductDTO dto) {
        return Product.builder()
                .platform(dto.getPlatform())
                .title(dto.getTitle())
                .price(dto.getPrice())
                .currency(dto.getCurrency())
                .condition(dto.getCondition())
                .imageUrl(dto.getImageUrl())
                .url(dto.getUrl())
                .stock(dto.getStock())
                .rating(dto.getRating())
                .reviewCount(dto.getReviewCount())
                .model(dto.getModel())
                .build();
    }
}
