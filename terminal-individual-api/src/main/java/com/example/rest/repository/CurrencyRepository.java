package com.example.rest.repository;

import com.example.rest.entity.RootEntity;
import com.example.rest.dto.dtoProjection.ExchangeRateDtoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<RootEntity, Long> {

    @Query(nativeQuery = true, value = "select * from get_last_exchange_rate()")
    Optional<ExchangeRateDtoProjection> getLastExchangeRate();

}
