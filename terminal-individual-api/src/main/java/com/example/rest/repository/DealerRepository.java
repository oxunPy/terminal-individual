package com.example.rest.repository;

import com.example.rest.entity.RootEntity;
import com.example.rest.dto.dtoProjection.DealerDtoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealerRepository extends JpaRepository<RootEntity, Long> {
    @Query(nativeQuery = true, value =  "select " +
                                        "id as id, " +
                                        "name as name, " +
                                        "dealer_code as dealerCode " +
                                        " from get_dealers_with_name_like(:name)")
    List<DealerDtoProjection> getDealersWithNameLike(@Param("name") String name);

    @Query(nativeQuery = true, value =  "select " +
                                        "id as id, " +
                                        "name as name, " +
                                        "dealer_code as dealerCode " +
                                        "from get_dealers_with_name_like(:name)", countProjection = "select count(*) from get_dealers_with_name_like(:name)")
    Page<DealerDtoProjection> pageDealers(String name, Pageable pageable);

    @Query(nativeQuery = true, value = "select count(*) from get_dealers_with_name_like(:name)")
    Integer count(@Param("name") String name);
}
