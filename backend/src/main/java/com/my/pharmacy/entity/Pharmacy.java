package com.my.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "pharmacy")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pharmacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeName;   // 약국 이름
    private double distance;    // 거리
    private double latitude;    // 위도
    private double longitude;   // 경도
    private String userAddress; // 사용자가 입력한 검색 위치
}
