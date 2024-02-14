package com.zoopbike.application.repo;

import com.zoopbike.application.entity.BikeProviderPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BikePartnerRepo extends JpaRepository <BikeProviderPartner, UUID>{
}
