package com.zoopbike.application.controller;

import com.zoopbike.application.dto.BikeReturnDto;
import com.zoopbike.application.dto.BookDto;
import com.zoopbike.application.service.impl.BikeFindingServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.geom.RectangularShape;
import java.util.List;
@RestController
@RequestMapping(value = "bike/search")
public class BikeSearchController {

    @Autowired
    BikeFindingServiceImpl bikeFindingService;

    @GetMapping(value = "/city/{city}")

    public ResponseEntity<List<BikeReturnDto>> getAllBikeByCity(@PathVariable("city") String city, @RequestBody  BookDto bookDto) {
        return ResponseEntity.ok(this.bikeFindingService.bikefindingProcess(city,0,5,bookDto));
    }
}
