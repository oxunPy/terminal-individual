package com.example.rest.repository;

import com.example.rest.dto.dtoProjection.CompanyDtoProjection;
import com.example.rest.dto.dtoProjection.CurrencyDtoProjection;
import com.example.rest.entity.RootEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends JpaRepository<RootEntity, Long> {

    @Query(nativeQuery = true, value = "select * from get_companies()", countProjection = "select count(*) from get_companies()")
    Page<CompanyDtoProjection> getCompanies(Pageable pageable);

    @Query(nativeQuery = true, value = "select * from currency where status = any(ARRAY[2, 5])")
    List<CurrencyDtoProjection> getAllCurrencies();

    @Query(nativeQuery = true, value = "select create_update_company(:company_name, :is_main, :phone, :motto, :director, :manager, :telegram_contact, :email, :company_id)")
    Long createOrUpdateCompany(@Param("company_name") String companyName, @Param("is_main") Boolean isMain, @Param("phone") String phone,
                               @Param("motto") String motto, @Param("director") String director, @Param("manager") String manager,
                               @Param("telegram_contact") String telegramContact, @Param("email") String email,
                               @Param("company_id") Long company_id);

    @Query(nativeQuery = true, value = "select save_as_file(:file_name, :org_file_name)")
    Long createAsFile(@Param("file_name") String fileName, @Param("org_file_name") String orgFileName);
    @Query(nativeQuery = true, value = "select set_company_img(:company_id, :logo_id, 'LOGO')")
    Integer setLogo(@Param("company_id") Long companyId, @Param("logo_id") Long logoId);

    @Query(nativeQuery = true, value = "select set_company_img(:company_id, :favicon_id, 'FAVICON')")
    Integer setFavicon(@Param("company_id") Long companyId, @Param("favicon_id") Long faviconId);

    @Query(nativeQuery = true, value = "select make_company_main(:company_id)")
    Boolean makeCompanyMain(@Param("company_id") Long companyId);

    @Query(nativeQuery = true, value = "select delete_company(:company_id)")
    Boolean deleteCompany(@Param("company_id") Long companyId);

    @Query(nativeQuery = true, value =  "select af.file_name\n" +
                                        "from company c\n" +
                                        "left join as_file af on c.logo_id = af.id\n" +
                                        "where c.is_main")
    String getMainCompanyLogoName();

    @Query(nativeQuery = true, value =  "select af.file_name\n" +
                                        "from company c\n" +
                                        "left join as_file af on c.favicon_id = af.id\n" +
                                        "where c.is_main")
    String getMainCompanyFaviconName();
}
