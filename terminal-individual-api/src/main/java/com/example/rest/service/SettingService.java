package com.example.rest.service;

import com.example.rest.model.form.Select2Form;
import com.example.rest.model.form.company.CompanyForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SettingService {

    DataTablesForm<CompanyForm> getCompanyDataTable(FilterForm filterForm, HttpServletRequest request);

    Boolean saveCompany(CompanyForm companyForm);

    Boolean editCompany(CompanyForm companyForm);

    Boolean deleteCompany(Long companyId);

    String saveCompanyLogoAndGetURI(HttpServletRequest request, Long companyId, MultipartFile file);

    String saveCompanyFaviconAndGetURI(HttpServletRequest request, Long companyId, MultipartFile file);

    List<Select2Form> findAllCompany();

    List<Select2Form> findAllCurrency();

    CompanyForm getCompanyById(HttpServletRequest request, Long id);

    String getMainCompanyLogoName();

    String getMainCompanyFaviconName();
}
