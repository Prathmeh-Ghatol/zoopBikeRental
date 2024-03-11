package com.zoopbike.application;

import com.zoopbike.application.entity.BikeBooking;
import com.zoopbike.application.repo.BikeBookingJpa;
import com.zoopbike.application.service.impl.BikeSeImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	BikeSeImpl bikeSe;

	@Test

	void contextLoads() {

	}


}
