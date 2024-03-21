package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.customexception.InvalidCityParameterException;
import com.codecool.solarwatch.model.entities.city.City;
import com.codecool.solarwatch.model.sunrise.SunriseCreationDTO;
import com.codecool.solarwatch.model.sunrise.SunriseReport;
import com.codecool.solarwatch.model.sunrise.SunriseUpdateDTO;
import com.codecool.solarwatch.service.CityService;
import com.codecool.solarwatch.service.SunriseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class SunriseController {
    private final CityService cityService;
    private final SunriseService sunriseService;

    public SunriseController(CityService cityService, SunriseService sunriseService) {
        this.cityService = cityService;
        this.sunriseService = sunriseService;
    }

    @GetMapping("/sunrise")
    public ResponseEntity<?> getSunrise(@RequestParam String city, @RequestParam(required = false) LocalDate date){
        try{
            if(city == null){
                throw new InvalidCityParameterException(city);
            }

            City searchedCity = cityService.getCity(city);
            LocalDate searchedDate = date == null ? LocalDate.now() : date;
            SunriseReport sunriseReport = sunriseService.getSunrise(searchedCity, searchedDate);

            return ResponseEntity.ok(sunriseReport);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping("/sunrise")
    public ResponseEntity<?> createSunrise(@RequestBody SunriseCreationDTO sunriseCreationDTO){
        try{
            sunriseService.createSunrise(sunriseCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/sunrise/{id}")
    public ResponseEntity<?> updateSunrise(@RequestBody SunriseUpdateDTO sunriseUpdateDTO, @PathVariable long id){
        try{
            sunriseService.updateSunrise(id, sunriseUpdateDTO);
            return ResponseEntity.ok(String.format("Sunrise with id: %s is updated!", id));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/sunrise/{id}")
    public ResponseEntity<?> deleteSunrise(@PathVariable long id){
        try{
            sunriseService.deleteSunrise(id);
            return ResponseEntity.ok(String.format("Sunrise with id: %s is deleted!", id));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
