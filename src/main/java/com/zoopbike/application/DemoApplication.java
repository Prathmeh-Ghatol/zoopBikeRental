package com.zoopbike.application;

import com.zoopbike.application.dto.BikeProviderPartnerDto;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.repo.BikePartnerRepo;
import com.zoopbike.application.service.impl.BikePartnerServiceImpl;
import com.zoopbike.application.service.impl.BikeSeImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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


	@Override
	public void run(String... args) throws Exception {

	}
}

