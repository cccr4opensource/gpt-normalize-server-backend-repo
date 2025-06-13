package com.cccr.gpt_normalize_server.service;

import com.cccr.gpt_normalize_server.entity.Product;
import com.cccr.gpt_normalize_server.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product save(Product product) {
        // 정규화 된 상품을 db에 저장
        return productRepository.save(product);
    }

    public Product findById(Long id) {
        // 저장된 상품을 단건 조회 ( 선택 기능 )
        return productRepository.findById(id).orElse(null);
    }

    // 필요시: 전체 목록 조회, 삭제, 업데이트 등 추가 가능
}
