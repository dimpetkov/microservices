package com.officemapmicroservice.service;

import com.googlecode.jmapper.JMapper;
import com.officemapmicroservice.dto.CityDto;
import com.officemapmicroservice.entities.City;
import com.officemapmicroservice.repositories.CitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {

    private final CitiesRepository citiesRepository;
@Autowired
    public CityService(CitiesRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
    }

    public List<CityDto> getCities() {
        JMapper<CityDto, City> cityDtoJMapper = new JMapper<>(CityDto.class, City.class);
        List<CityDto> citiesDtoList = new ArrayList<>();
        citiesRepository.findAll().forEach(city -> citiesDtoList.add(cityDtoJMapper.getDestination(city)));
        return citiesDtoList;
    }
}
