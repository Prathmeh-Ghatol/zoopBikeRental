package com.zoopbike.application.repo;

import com.zoopbike.application.entity.CurrentAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CurrentAddressRepo extends JpaRepository<CurrentAddress, UUID> {
}
