package com.my.pharmacy.service;

import com.my.pharmacy.dto.DocumentDto;
import com.my.pharmacy.dto.KakaoApiResponseDto;
import com.my.pharmacy.dto.OutputDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoCategorySearchService {
    private final RestTemplate restTemplate;

    // 환경변수에서 ${KAKAO_REST_API_KEY} 값을 가져와서 사용
    @Value("${KAKAO_REST_API_KEY}")
    private String kakaoRestApiKey;

    // 우리가 만들 주소(url)
    // https://dapi.kakao.com/v2/local/search/category.json
    // ?category_group_code=PM9&x=127.026692446306&y=37.4987750083767&radius=1000&sort=distance
    private static final String KAKAO_CATEGORY_URL =
            "https://dapi.kakao.com/v2/local/search/category.json";

    public KakaoApiResponseDto resultCategorySearch(
            double latitude, double longitude, double radius) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(KAKAO_CATEGORY_URL);
        // 파라미터 설정
        uriBuilder.queryParam("category_group_code", "PM9");
        uriBuilder.queryParam("x", longitude);
        uriBuilder.queryParam("y", latitude);
        uriBuilder.queryParam("radius", radius);
        uriBuilder.queryParam("sort", "distance");

        URI uri = uriBuilder.build().encode().toUri();
        // 헤더 만들기
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION,
                "KakaoAK " + kakaoRestApiKey);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                KakaoApiResponseDto.class
        ).getBody();
    }

    public List<OutputDto> makeOutputDto(List<DocumentDto> documentList) {
        // 리스트에 3개만 담기
        return documentList.stream()
                .map(x -> convertDto(x))
                .limit(3)
                .toList();
    }

    // DocumentDto에서 자료를 뽑아서 길찾기, 로드맵을 추가
    private OutputDto convertDto(DocumentDto documentDto) {
        String ROAD_VIEW_URL = "https://map.kakao.com/link/roadview/";
        String DIRECTION_URL = "https://map.kakao.com/link/to/";

        String params = String.join(",", documentDto.getPlaceName(),
                String.valueOf(documentDto.getLatitude()),
                String.valueOf(documentDto.getLongitude()));
        String mapUrl = UriComponentsBuilder
                .fromUriString(DIRECTION_URL + params)
                .toUriString();

        String roadUrl = ROAD_VIEW_URL + documentDto.getLatitude() + "," +
                documentDto.getLongitude();

        return OutputDto.builder()
                .pharmacyName(documentDto.getPlaceName())
                .pharmacyAddress(documentDto.getAddressName())
                .directionURL(mapUrl)
                .roadViewURL(roadUrl)
                .distance(String.format("%.0f m", documentDto.getDistance()))
                // ✅ 위도, 경도 추가
                .latitude(documentDto.getLatitude())
                .longitude(documentDto.getLongitude())
                .build();
    }
}