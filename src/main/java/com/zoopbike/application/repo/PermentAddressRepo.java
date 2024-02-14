package com.zoopbike.application.repo;

import com.zoopbike.application.dto.PermenetAddressDto;
import com.zoopbike.application.entity.Permanentaddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermentAddressRepo extends JpaRepository<Permanentaddress , UUID> {
}
