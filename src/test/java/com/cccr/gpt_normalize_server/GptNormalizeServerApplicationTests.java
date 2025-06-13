package com.cccr.gpt_normalize_server;

import com.cccr.gpt_normalize_server.entity.Product;
import com.cccr.gpt_normalize_server.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class GptNormalizeServerApplicationTests {

	// 직접 생성하면 안 되고 Spring이 주입해줘야 함
	@Autowired
	private ProductService productService;

	// 가데이터로 save가 되는지 확인
	@Test
	void testSaveProduct() {
		Product product = Product.builder()
				.platform("eBay")
				.title("Dell R730xd")
				.price(500.0)
				.currency("USD")
				.build();

		Product saved = productService.save(product);
		assertNotNull(saved.getId());
	}

}
