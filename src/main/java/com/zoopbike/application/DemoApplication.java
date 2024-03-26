package com.zoopbike.application;

import com.zoopbike.application.dto.BikeProviderPartnerDto;
import com.zoopbike.application.entity.BikeBooking;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.repo.BikeBookingJpa;
import com.zoopbike.application.repo.BikePartnerRepo;
import com.zoopbike.application.service.impl.BikePartnerServiceImpl;
import com.zoopbike.application.service.impl.BikeSeImpl;
import com.zoopbike.application.utils.zoopBikeRentalApplicationConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.UUID;

import static com.zoopbike.application.utils.zoopBikeRentalApplicationConstant.RoleMaps;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner{

	@Autowired
	BikePartnerServiceImpl service;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Autowired
	zoopBikeRentalApplicationConstant zoopBikeRentalApplicationConstant ;


	@Override
	public void run(String... args) throws Exception {
//		List<List<BikeBooking>>allBooking =service.getBookedBikeStatus(UUID.fromString("7b9467a3-9010-4b52-9*.369090-1a6e1d92e0c5"));


	}
}

