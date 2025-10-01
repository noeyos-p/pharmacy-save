package com.my.pharmacy.repository;

import com.my.pharmacy.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository <Pharmacy, Long> {
}
