package com.zoopbike.application.repo;

import com.zoopbike.application.dto.BookingRecords;
import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.entity.Bike;
import com.zoopbike.application.utils.SqlQuery;
import org.aspectj.weaver.ast.Var;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.w3c.dom.stylesheets.LinkStyle;


import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.zoopbike.application.utils.SqlQuery.*;

public interface ApplicationUserRepo extends JpaRepository<ApplicationUser, UUID> {

    @Query(value = findApplicationUserByEmail, nativeQuery = true)
    ApplicationUser findApplicationUserByEmail(@Param("email") String email);

    @Query(value = GetAllApplicationUser, nativeQuery = true)
    Page<ApplicationUser> getAllapplicationUser(Pageable pageable);

    @Query(value = getAllBookingForApplicationUser, nativeQuery = true)
    List<BookingRecords> getAllBookings(@Param("ApplicationId") UUID applicationId);

    @Query(value = getBikeByBookingId, nativeQuery = true)
    byte[] getAllBikes(@Param("bookid") UUID bookingId);


}
