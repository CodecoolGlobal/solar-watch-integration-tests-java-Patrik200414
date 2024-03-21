package com.codecool.solarwatch.controller;


import com.codecool.solarwatch.model.dto.user.UserAuthenticationDTO;
import com.codecool.solarwatch.model.jwtresponse.JwtResponse;
import com.codecool.solarwatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserAuthenticationDTO userRegistrationDTO){
        try{
            userService.registrate(userRegistrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DataIntegrityViolationException e){
            return ResponseEntity.badRequest().body(new Exception(String.format("User with username '%s' already exists!", userRegistrationDTO.userName())));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody UserAuthenticationDTO userAuthenticationDTO){
            try{
                JwtResponse jwtResponse = userService.login(userAuthenticationDTO);
                return ResponseEntity.ok(jwtResponse);
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
            }
    }


    @PutMapping("/role/{id}")
    public ResponseEntity<?> addAdminRole(@PathVariable long id){
        try{
            JwtResponse jwtResponse = userService.addAdminRole(id);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
