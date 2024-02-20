package com.zoopbike.application.repo;

import com.zoopbike.application.entity.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Set;
import java.util.UUID;

import static com.zoopbike.application.utils.SqlQuery.GetAllApplicationUser;
import static com.zoopbike.application.utils.SqlQuery.findApplicationUserByEmail;

public interface ApplicationUserRepo extends JpaRepository<ApplicationUser , UUID> {

    @Query(value = findApplicationUserByEmail,nativeQuery = true)
    ApplicationUser findApplicationUserByEmail(@Param("email") String email);

    @Query(value =GetAllApplicationUser,nativeQuery = true)
    Page<ApplicationUser> getAllapplicationUser(Pageable pageable);


}
