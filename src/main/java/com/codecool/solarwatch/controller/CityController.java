package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.openweather.GeoCode;
import com.codecool.solarwatch.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping("/city")
    public ResponseEntity<?> createCity(@RequestBody GeoCode city){
        try{
            cityService.saveCity(city);
            return ResponseEntity.ok(String.format("City is saved!"));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/city/{id}")
    public ResponseEntity<?> updateCity(@PathVariable long id, @RequestBody GeoCode updatedCity){
        try{
            cityService.updateCity(id, updatedCity);
            return ResponseEntity.ok(String.format("City with id: %s is updated!", id));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/city/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable long id){
        try{
            cityService.deleteCity(id);
            return ResponseEntity.ok(String.format("City with id: %s and all the references are deleted!", id));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
