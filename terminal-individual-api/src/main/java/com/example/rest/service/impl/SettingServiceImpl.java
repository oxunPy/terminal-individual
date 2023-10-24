package com.example.rest.service.impl;

import com.example.rest.constant.IMG_TYPE;
import com.example.rest.dto.dtoProjection.CompanyDtoProjection;
import com.example.rest.model.form.Select2Form;
import com.example.rest.model.form.company.CompanyForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.model.form.tables.OrderForm;
import com.example.rest.repository.SettingRepository;
import com.example.rest.service.SettingService;
import com.example.rest.utils.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;

    @Value("${upload.folder.img}")
    private String IMG_FOLDER;

    @Override
    @Transactional(readOnly = true)
    public DataTablesForm<CompanyForm> getCompanyDataTable(FilterForm filter, HttpServletRequest request) {
        Sort sort = getSort(filter.orderMap());

        if (sort.isEmpty()) {
            sort.and(Sort.Order.asc("id"));
        }

        PageRequest pageRequest = PageRequest.of(filter.getStart() / filter.getLength(), filter.getLength(), sort);

        Page<CompanyDtoProjection> pageCompanies = settingRepository.getCompanies(pageRequest);

        List<CompanyForm> companyList = pageCompanies.stream()
                .map(c -> CompanyForm.builder().id(c.getId())
                                               .faviconUrl(RequestUtil.getRequestURI(request, "/img/company/" + c.getFaviconImgName()))
                                               .logoImgUrl(RequestUtil.getRequestURI(request, "/img/company/" + c.getLogoImgName()))
                                               .companyName(c.getCompanyName())
                                               .email(c.getEmail())
                                               .manager(c.getManager())
                                               .motto(c.getMotto())
                                               .director(c.getDirector())
                                               .telegramContact(c.getTelegramContact())
                                               .isMain(c.getIsMain())
                                               .phoneNumber(c.getPhoneNumber()).build()
                   ).collect(Collectors.toList());

        DataTablesForm<CompanyForm> dataTable = new DataTablesForm<>();
        dataTable.setData(companyList);
        dataTable.setDraw(filter.getDraw());
        dataTable.setRecordsTotal((int) pageCompanies.getTotalElements());
        dataTable.setRecordsFiltered((int) pageCompanies.getTotalElements());
        return dataTable;
    }

    @Override
    public Boolean saveCompany(CompanyForm form){
        if(form == null) return null;
        Long newCompanyId = settingRepository.createOrUpdateCompany(form.getCompanyName(), form.getIsMain() == null, form.getPhoneNumber(), form.getMotto(), form.getDirector(), form.getManager(), form.getTelegramContact(), form.getEmail(), null);
        // # save company logo and favicon
        if(form.getFavicon() != null) saveFile(form.getFavicon(), newCompanyId, IMG_TYPE.FAVICON);
        if(form.getLogo() != null) saveFile(form.getLogo(), newCompanyId, IMG_TYPE.LOGO);

        if(form.getIsMain()) setCompanyMain(newCompanyId);
        return newCompanyId != null;
    }

    @Override
    public Boolean editCompany(CompanyForm edited){
        if(edited == null || edited.getId() == null) return null;
        CompanyForm existingForm = getCompanyById(null, edited.getId());
        if(edited.getIsMain() != null && edited.getIsMain()){
            setCompanyMain(edited.getId());
        }
        if(edited.getDirector() != null) existingForm.setDirector(edited.getDirector());
        if(edited.getCompanyName() != null) existingForm.setCompanyName(edited.getCompanyName());
        if(edited.getEmail() != null) existingForm.setEmail(edited.getEmail());
        if(edited.getManager() != null) existingForm.setManager(edited.getManager());
        if(edited.getMotto() != null) existingForm.setMotto(edited.getMotto());
        if(edited.getPhoneNumber() != null) existingForm.setPhoneNumber(edited.getPhoneNumber());
        if(edited.getTelegramContact() != null) existingForm.setTelegramContact(edited.getTelegramContact());
        existingForm.setIsMain(edited.getIsMain() != null && existingForm.getIsMain());

        if(edited.getLogo() != null && !edited.getLogo().isEmpty()) saveFile(edited.getLogo(), edited.getId(), IMG_TYPE.LOGO);
        if(edited.getFavicon() != null && !edited.getFavicon().isEmpty()) saveFile(edited.getFavicon(), edited.getId(), IMG_TYPE.FAVICON);

        return Objects.equals(
                settingRepository.createOrUpdateCompany(existingForm.getCompanyName(), existingForm.getIsMain(), existingForm.getPhoneNumber(), existingForm.getMotto(),
                                                        existingForm.getDirector(), existingForm.getManager(), existingForm.getTelegramContact(), existingForm.getEmail(), existingForm.getId()),
                edited.getId());
    }

    @Override
    public Boolean deleteCompany(Long companyId){
        return settingRepository.deleteCompany(companyId);
    }

    private boolean setCompanyMain(Long companyId){
        return settingRepository.makeCompanyMain(companyId);
    }

    @Override
    public String saveCompanyLogoAndGetURI(HttpServletRequest request, Long companyId, MultipartFile multipartFile) {
        String filePath = null;
        if (multipartFile != null && !multipartFile.isEmpty()) {
            filePath = saveFile(multipartFile, companyId, IMG_TYPE.LOGO);              // saves files and return fileNames
        }
        return getFileUrls(request, new String[]{filePath}).get(0);
    }

    @Override
    public String saveCompanyFaviconAndGetURI(HttpServletRequest request, Long companyId, MultipartFile multipartFile) {
        String filePath = null;
        if (multipartFile != null && !multipartFile.isEmpty()) {
            filePath = saveFile(multipartFile, companyId, IMG_TYPE.FAVICON);              // saves files and return fileNames
        }
        return getFileUrls(request, new String[]{filePath}).get(0);
    }

    public String saveFile(MultipartFile file, Long companyId, IMG_TYPE imgType) {                      // save files and return fileNames
        File folder = new File(System.getProperty("user.home") + IMG_FOLDER);
        if(!folder.exists()) folder.mkdirs();

        String filePath = null;
        try {
            String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename().replaceAll("[+]", " ");
            filePath = newFileName;
            Files.copy(file.getInputStream(), Paths.get(System.getProperty("user.home") + IMG_FOLDER).resolve(newFileName), StandardCopyOption.REPLACE_EXISTING);

            // #save file and set it on company
            Long fileId = settingRepository.createAsFile(newFileName, file.getOriginalFilename());
            if(imgType == IMG_TYPE.FAVICON) setCompanyFavicon(companyId, fileId);
            if(imgType == IMG_TYPE.LOGO) setCompanyLogo(companyId, fileId);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    private List<String> getFileUrls(HttpServletRequest request, String[] filePaths) {
        if (filePaths == null) return new ArrayList<>();

        List<String> fileUrls = new ArrayList<>();
        for (String filePath : filePaths) {
            fileUrls.add(RequestUtil.getRequestURL(request, "/api/mobile/message/file/" + URLEncoder.encode(filePath)));
        }
        return fileUrls;
    }

    private void setCompanyLogo(Long companyId, Long fileId){
        settingRepository.setLogo(companyId, fileId);
    }

    private void setCompanyFavicon(Long companyId, Long fileId){
        settingRepository.setFavicon(companyId, fileId);
    }

    @Override
    public CompanyForm getCompanyById(HttpServletRequest request, Long id) {
        Optional<CompanyDtoProjection> companyOpt = settingRepository.getCompanies(PageRequest.of(0, Integer.MAX_VALUE)).stream().filter(c -> c.getId() == id).findFirst();
        return companyOpt.map(c ->
                CompanyForm.builder().id(c.getId())
                .faviconUrl(request != null ? RequestUtil.getRequestURI(request, "/img/company/" + c.getFaviconImgName()) : null)
                .logoImgUrl(request != null ? RequestUtil.getRequestURI(request, "/img/company/" + c.getLogoImgName()) : null)
                .companyName(c.getCompanyName())
                .email(c.getEmail())
                .manager(c.getManager())
                .motto(c.getMotto())
                .director(c.getDirector())
                .telegramContact(c.getTelegramContact())
                .isMain(c.getIsMain())
                .phoneNumber(c.getPhoneNumber()).build()).orElse(null);
    }

    @Override
    public List<Select2Form> findAllCompany() {
        return settingRepository.getCompanies(PageRequest.of(0, Integer.MAX_VALUE)).getContent().stream()
                .map(c -> new Select2Form(c.getId(), c.getCompanyName())).collect(Collectors.toList());
    }

    @Override
    public List<Select2Form> findAllCurrency(){
        return settingRepository.getAllCurrencies().stream().map(c -> new Select2Form(c.getId(), c.getCode())).collect(Collectors.toList());
    }

    @Override
    public String getMainCompanyLogoName(){
        return settingRepository.getMainCompanyLogoName();
    }

    @Override
    public String getMainCompanyFaviconName(){
        return settingRepository.getMainCompanyFaviconName();
    }

    private Sort getSort(Map<String, OrderForm> orderMap) {
        List<Sort.Order> orders = new ArrayList<>();
        if(orderMap != null){
            orderMap.forEach((column, orderForm) -> {
                String property = null;

                switch (column) {
                    case "name":
                        property = "name";
                        break;
                    case "director":
                        property = "director";
                        break;
                    case "manager":
                        property = "manager";
                        break;
                }
                Sort.Direction direction = Sort.Direction.fromString(orderForm.getDir());
                orders.add(direction.isAscending() ? Sort.Order.asc(Objects.requireNonNull(property)) : Sort.Order.desc(Objects.requireNonNull(property)));
            });
        }
        return Sort.by(orders);
    }
}
