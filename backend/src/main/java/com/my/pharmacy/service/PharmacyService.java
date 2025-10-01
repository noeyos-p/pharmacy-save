package com.my.pharmacy.service;

import com.my.pharmacy.entity.Pharmacy;
import com.my.pharmacy.repository.PharmacyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PharmacyService {
    private final PharmacyRepository pharmacyRepository;

    public PharmacyService(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    // 약국 저장
    public Pharmacy savePharmacy(Pharmacy pharmacy) {
        return pharmacyRepository.save(pharmacy);
    }

    // 전체 조회
    public List<Pharmacy> getAllPharmacies() {
        return pharmacyRepository.findAll();
    }
}
