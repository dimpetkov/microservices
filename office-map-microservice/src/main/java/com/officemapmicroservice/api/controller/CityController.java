package com.officemapmicroservice.api.controller;

import com.officemapmicroservice.service.CityService;
import com.officemapmicroservice.dto.CityDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

//    @GetMapping("/")
//    public List<CityDto> getAllCities() {
//        return cityService.getCities();
//    }
}
