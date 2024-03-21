package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.dto.user.Role;
import com.codecool.solarwatch.model.dto.user.UserAuthenticationDTO;
import com.codecool.solarwatch.model.entity.user.UserEntity;
import com.codecool.solarwatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrate(UserAuthenticationDTO userAuthenticationDTO){
        UserEntity userEntity = new UserEntity(userAuthenticationDTO.userName(),
                passwordEncoder.encode(userAuthenticationDTO.password()),
                Set.of(Role.USER)
        );

        userRepository.save(userEntity);
    }


}
