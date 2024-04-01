package com.codecool.solarwatch.service;

import com.codecool.solarwatch.customexception.NonExistingUserException;
import com.codecool.solarwatch.model.dto.user.Role;
import com.codecool.solarwatch.model.dto.user.UserAuthenticationDTO;
import com.codecool.solarwatch.model.entity.user.UserEntity;
import com.codecool.solarwatch.model.jwtresponse.JwtResponse;
import com.codecool.solarwatch.repository.UserRepository;
import com.codecool.solarwatch.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public void registrate(UserAuthenticationDTO userAuthenticationDTO){
        UserEntity userEntity = new UserEntity(userAuthenticationDTO.userName(),
                passwordEncoder.encode(userAuthenticationDTO.password()),
                Set.of(Role.ROLE_USER)
        );

        userRepository.save(userEntity);
    }

    public JwtResponse login(UserAuthenticationDTO userAuthenticationDTO){
        Authentication authentication = authenticateUser(
                userAuthenticationDTO.userName(),
                userAuthenticationDTO.password()
        );

        String jwt = jwtUtils.generateJwtToken(authentication);
        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new JwtResponse(jwt, userDetails.getUsername(), roles);
    }

    public JwtResponse addAdminRole(long id){
        Optional<UserEntity> searchedUser = userRepository.findById(id);
        if(searchedUser.isEmpty()){
            throw new NonExistingUserException(id);
        }

        UserEntity user = searchedUser.get();
        Set<Role> userRoles = user.getRoles();
        userRoles.add(Role.ROLE_ADMIN);

        user.setRoles(userRoles);
        UserEntity savedUser = userRepository.save(user);


        String jwt = jwtUtils.generateJwtToken(savedUser);
        List<String> roles = savedUser.getRoles().stream()
                .map(Enum::name)
                .toList();

        return new JwtResponse(jwt, savedUser.getUserName(), roles);
    }

    private Authentication authenticateUser(String userName, String password){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userName,
                password
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

}
