package com.cccr.gpt_normalize_server.repository;

import com.cccr.gpt_normalize_server.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.reactive.function.client.WebClient;


// spring data jpa를 사용하여 Product 엔티티와 연동되는 저장소(Repository)를 정의한 것
// 별도의 메서드 없이도 findAll(), findById(), save(Product product), deleteById 등의 기본 메서드를 자동으로 제공한다.
public interface ProductRepository extends JpaRepository<Product, Long> {
}
