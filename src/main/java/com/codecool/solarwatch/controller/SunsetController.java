package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.custom_exception.InvalidCityParameterException;
import com.codecool.solarwatch.model.entities.city.City;
import com.codecool.solarwatch.model.sunset.SunsetCreationDTO;
import com.codecool.solarwatch.model.sunset.SunsetReport;
import com.codecool.solarwatch.model.sunset.SunsetUpdateDTO;
import com.codecool.solarwatch.service.CityService;
import com.codecool.solarwatch.service.SunsetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class SunsetController {

    private final SunsetService sunsetService;
    private final CityService cityService;

    @Autowired
    public SunsetController(SunsetService sunsetService, CityService cityService) {
        this.sunsetService = sunsetService;
        this.cityService = cityService;
    }

    @GetMapping("/sunset")
    public ResponseEntity<?> getsunSet(@RequestParam String city, @RequestParam(required = false) LocalDate date){
        try{
            if(city == null){
                throw new InvalidCityParameterException(city);
            }

            City searchedCity = cityService.getCity(city);

            LocalDate searchedDate = date == null ? LocalDate.now() : date;
            SunsetReport sunsetReport = sunsetService.getSunset(searchedCity, searchedDate);
            return ResponseEntity.ok(sunsetReport);

        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/sunset")
    public ResponseEntity<?> createSunset(@RequestBody SunsetCreationDTO sunsetCreationDTO){
         try{
            sunsetService.createSunset(sunsetCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
         } catch (Exception e){
             return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
         }
    }

    @PutMapping("/sunset/{id}")
    public ResponseEntity<?> updateSunset(@RequestBody SunsetUpdateDTO sunsetUpdateDTO, @PathVariable long id){
        try{
            sunsetService.updateSunset(id, sunsetUpdateDTO);
            return ResponseEntity.ok(String.format("Sunset with id: %s is updated!", id));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/sunset/{id}")
    public ResponseEntity<?> deleteSunset(@PathVariable long id){
        try{
            sunsetService.deleteSunset(id);
            return ResponseEntity.ok(String.format("Sunrise with id: %s is deleted!", id));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
