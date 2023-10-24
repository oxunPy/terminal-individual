package com.example.rest.repository;

import com.example.rest.entity.RootEntity;
import com.example.rest.dto.dtoProjection.AccountDtoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<RootEntity, Long> {

    @Query(nativeQuery = true, value = "select * from get_accounts_with_type(:type, :active, :updated)")
    List<AccountDtoProjection> getAllAccountsWithType(@Param("type") int type,
                                                      @Param("active") int active,
                                                      @Param("updated") int updated);

    @Query(nativeQuery = true, value = "select * from get_accounts_with_type_and_name_like(:type, :name, :active, :updated)")
    List<AccountDtoProjection> getAllAccountsWithTypeAndNameLike(@Param("type") int type,
                                                                 @Param("name") String name,
                                                                 @Param("active") int active,
                                                                 @Param("updated") int updated);

    @Query(nativeQuery = true, value = "select * from get_accounts_with_type(:type, :active, :updated)",
                               countProjection = "select count(*) from get_accounts_with_type(:type, :active, :updated)")
    Page<AccountDtoProjection> pageAllAccountsWithType(@Param("type") int type,
                                                       @Param("active") int active,
                                                       @Param("updated") int updated, Pageable pageable);
}
