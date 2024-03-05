package com.zoopbike.application.repo;

import com.zoopbike.application.entity.CurrentAddress;
import com.zoopbike.application.utils.SqlQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CurrentAddressRepo extends JpaRepository<CurrentAddress, UUID> {
    @Query(value = SqlQuery.findBikeByCity,nativeQuery = true)
    public Page<CurrentAddress> getAllBikeFromCity(@Param(value = "city") String city, Pageable pageable);
}
