package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.entity.user.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserName(String userName);

}
