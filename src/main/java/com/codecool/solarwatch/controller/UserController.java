package com.codecool.solarwatch.controller;


import com.codecool.solarwatch.model.jwtresponse.JwtResponse;
import com.codecool.solarwatch.model.dto.user.UserAuthenticationDTO;
import com.codecool.solarwatch.security.jwt.JwtUtils;
import com.codecool.solarwatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
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
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        userAuthenticationDTO.userName(),
                        userAuthenticationDTO.password()
                ));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication);

                User userDetails = (User) authentication.getPrincipal();
                List<String> roles = userDetails
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

                return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), roles));
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
            }
    }

    @PutMapping("/role")
    public ResponseEntity<?> addAdminRole(){
        try{

        } catch (Exception e){

        }
    }
}
