package com.zoopbike.application.repo;

import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.utils.SqlQuery;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface BikePartnerRepo extends JpaRepository <BikeProviderPartner, UUID>{

    @Query(value = SqlQuery.findBikePattnerProviderByEmail,nativeQuery = true)
    public BikeProviderPartner  findBikeProviderPartner(@Param("email") String email);


}
