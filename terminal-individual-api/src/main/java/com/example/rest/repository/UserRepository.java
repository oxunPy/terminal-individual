package com.example.rest.repository;

import com.example.rest.entity.RootEntity;
import com.example.rest.dto.dtoProjection.UserDtoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<RootEntity, Long> {

    @Query(nativeQuery = true, value = "select * from get_user(:login, :password)")
    Optional<UserDtoProjection> getUser(@Param("login") String login, @Param("password") String password);

    @Query(nativeQuery = true, value = "select * from get_user_by_login(:login)")
    Optional<UserDtoProjection> findByLogin(@Param("login") String login);
}
