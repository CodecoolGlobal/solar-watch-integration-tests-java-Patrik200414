package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.user.Role;
import com.codecool.solarwatch.model.jwtresponse.JwtResponse;
import com.codecool.solarwatch.model.entity.user.UserEntity;
import com.codecool.solarwatch.model.user.dto.UserAuthenticationDTO;
import com.codecool.solarwatch.repository.UserRepository;
import com.codecool.solarwatch.security.jwt.JwtUtils;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserAuthenticationDTO userRegistrationDTO){
        try{
            UserEntity userEntity = new UserEntity(userRegistrationDTO.userName(),
                    passwordEncoder.encode(userRegistrationDTO.password()),
                    Set.of(Role.USER)
            );

            userRepository.save(userEntity);
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
}
