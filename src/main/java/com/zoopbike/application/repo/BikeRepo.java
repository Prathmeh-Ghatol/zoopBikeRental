package com.zoopbike.application.repo;

import com.zoopbike.application.entity.Bike;
import com.zoopbike.application.entity.CurrentAddress;
import com.zoopbike.application.utils.SqlQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.UUID;

public interface BikeRepo extends JpaRepository<Bike, UUID> {

    @Query(value = SqlQuery.GetAllBikeOfBikeVender,nativeQuery = true)
    Page<Bike>getAllBikeOfBikeProvider(Pageable pageable, @Param("id") UUID id);

}
