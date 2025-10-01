package com.my.pharmacy.controller;

import com.my.pharmacy.dto.DocumentDto;
import com.my.pharmacy.dto.InputDto;
import com.my.pharmacy.dto.KakaoApiResponseDto;
import com.my.pharmacy.dto.OutputDto;
import com.my.pharmacy.entity.Pharmacy;
import com.my.pharmacy.service.KakaoAddressSearchService;
import com.my.pharmacy.service.KakaoCategorySearchService;
import com.my.pharmacy.service.PharmacyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FormController {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final KakaoCategorySearchService kakaoCategorySearchService;
    private final PharmacyService pharmacyService;

    public FormController(KakaoAddressSearchService kakaoAddressSearchService,
                          KakaoCategorySearchService kakaoCategorySearchService,
                          PharmacyService pharmacyService) {
        this.kakaoAddressSearchService = kakaoAddressSearchService;
        this.kakaoCategorySearchService = kakaoCategorySearchService;
        this.pharmacyService = pharmacyService;
    }

    // ✅ 단순 확인용 API
    @GetMapping("/health")
    public String healthCheck() {
        return "Pharmacy API is running!";
    }

    // ✅ 검색 API (JSON 응답)
    @PostMapping("/search")
    public List<OutputDto> searchAddress(@RequestBody InputDto dto) {
        // 주소 검색 API 호출
        KakaoApiResponseDto kakaoApiResponseDto =
                kakaoAddressSearchService.requestAddressSearch(dto.getAddress());

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        // 카테고리 검색
        double radius = 1000;
        KakaoApiResponseDto recommendationDto =
                kakaoCategorySearchService.resultCategorySearch(
                        documentDto.getLatitude(),
                        documentDto.getLongitude(),
                        radius);

        // 검색 결과 DTO 변환
        List<OutputDto> outputDtoList =
                kakaoCategorySearchService.makeOutputDto(
                        recommendationDto.getDocumentList());

        // ✅ DB 저장
        for (OutputDto pharmacyDto : outputDtoList) {
            String distanceStr = pharmacyDto.getDistance().replace(" m", "").trim();
            Pharmacy pharmacy = Pharmacy.builder()
                    .placeName(pharmacyDto.getPharmacyName())
                    .latitude(pharmacyDto.getLatitude())
                    .longitude(pharmacyDto.getLongitude())
                    .distance(Double.parseDouble(distanceStr))
                    .userAddress(dto.getAddress())
                    .build();

            pharmacyService.savePharmacy(pharmacy);
        }

        // ✅ JSON으로 결과 반환
        return outputDtoList;
    }
}


/**
 * Thymeleaf로 확인용 Controller
 * */
/*package com.my.pharmacy.controller;

import com.my.pharmacy.dto.DocumentDto;
import com.my.pharmacy.dto.InputDto;
import com.my.pharmacy.dto.KakaoApiResponseDto;
import com.my.pharmacy.dto.OutputDto;
import com.my.pharmacy.entity.Pharmacy;
import com.my.pharmacy.service.KakaoAddressSearchService;
import com.my.pharmacy.service.KakaoCategorySearchService;
import com.my.pharmacy.service.PharmacyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class FormController {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final KakaoCategorySearchService kakaoCategorySearchService;
    private final PharmacyService pharmacyService;

    public FormController(KakaoAddressSearchService kakaoAddressSearchService,
                          KakaoCategorySearchService kakaoCategorySearchService,
                          PharmacyService pharmacyService) {
        this.kakaoAddressSearchService = kakaoAddressSearchService;
        this.kakaoCategorySearchService = kakaoCategorySearchService;
        this.pharmacyService = pharmacyService;
    }

    // ✅ 메인 페이지 (주소 입력 화면)
    @GetMapping("/")
    public String mainPage() {
        return "main"; // templates/main.html 열기
    }

    // ✅ 검색 결과 페이지
    @PostMapping("/search")
    public String searchAddress(InputDto dto, Model model) {
        // 주소 검색 API 호출
        KakaoApiResponseDto kakaoApiResponseDto =
                kakaoAddressSearchService.requestAddressSearch(dto.getAddress());

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        // 카테고리 검색
        double radius = 1000;
        KakaoApiResponseDto recommendationDto =
                kakaoCategorySearchService.resultCategorySearch(
                        documentDto.getLatitude(),
                        documentDto.getLongitude(),
                        radius);

        // 검색 결과 DTO 변환
        List<OutputDto> outputDtoList =
                kakaoCategorySearchService.makeOutputDto(
                        recommendationDto.getDocumentList());

        // ✅ DB 저장
        for (OutputDto pharmacyDto : outputDtoList) {
            String distanceStr = pharmacyDto.getDistance().replace(" m", "").trim();
            Pharmacy pharmacy = Pharmacy.builder()
                    .placeName(pharmacyDto.getPharmacyName())
                    .latitude(pharmacyDto.getLatitude())
                    .longitude(pharmacyDto.getLongitude())
                    .distance(Double.parseDouble(distanceStr))
                    .userAddress(dto.getAddress())
                    .build();

            pharmacyService.savePharmacy(pharmacy);
        }

        // ✅ 화면에 검색 결과 전달
        model.addAttribute("outputList", outputDtoList);

        return "output"; // templates/output.html 열기
    }
}*/