package com.zoopbike.application.repo;

import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.utils.SqlQuery;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.w3c.dom.stylesheets.LinkStyle;

import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;


public interface BikePartnerRepo extends JpaRepository <BikeProviderPartner, UUID>{

    @Query(value = SqlQuery.findBikePattnerProviderByEmail,nativeQuery = true)
    public BikeProviderPartner  findBikeProviderPartner(@Param("email") String email);


    @Query(value = SqlQuery.getAllBikeProviderInCity ,nativeQuery = true)
    public BikeProviderPartner getAll (@Param("currentAddressId") UUID currentId);



}
