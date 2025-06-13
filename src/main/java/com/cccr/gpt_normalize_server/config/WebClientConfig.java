package com.cccr.gpt_normalize_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /**
     * 왜 주입해야하는가?
     * WebClient는 외부 API ( GPT 서버등)에 HTTP 요청을 보내는 객체입니다.
     * 직접 만들어도 되지만...
     * 매번 new WebClient()를 하면
     * 요청마다 새 객체가 생성됨 -> 메모리 낭비
     * 커넥션 풀 공유가 안됨 -> 성능 저하
     * 재사용 불가능 -> 테스트 어려움
     * 그래서 Bean으로 등록하면 Spring이 WebClient 객체를 싱글톤 Bean으로 관리하게 된다.
     * 이제 GptService에서 @Autowired 없이 final WebClient webClient만 써도,
     * Spring이 자동으로 그 Bean을 생성자 주입해줍니다.
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
