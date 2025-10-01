package com.my.pharmacy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class OutputDto {
    private String pharmacyName;
    private String pharmacyAddress;
    private String directionURL; // 길 안내 url
    private String roadViewURL; // 로드뷰
    private String distance;
    private double latitude;    // 위도
    private double longitude;


}
