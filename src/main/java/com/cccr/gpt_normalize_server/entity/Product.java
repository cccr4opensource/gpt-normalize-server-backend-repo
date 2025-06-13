package com.cccr.gpt_normalize_server.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity // 이 클래스는 JPA에서 데이터베이스의 테이블과 매핑되는 엔티티임을 뜻한다.
@Getter // Lombok이 제공하는 어노테이션, 모든 필드에 대해 getter 메서드를 자동으로 제공한다.
@Setter // setter 메서드를 자동으로 생성해준다.
@NoArgsConstructor // 모든 필드에 대해 기본 생성자를 생성한다.
@AllArgsConstructor // 모든 필드에 대해 생성자를 생성한다.
@Builder // Lombok이 제공하는 어노테이션으로, 빌더 패턴을 사용할 수 있게 해줍니다.

public class Product {

    @Id // 이 필드가 기본키임을 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment를 사용한다.
    private Long id;

    private String platform;
    private String title;
    private String model;
    private String url;
    private String imageUrl;
    private double price;
    private String currency;

    @Column(name = "`condition`")
    private String condition;
    private int stock;
    private float rating;
    private int reviewCount;
}
