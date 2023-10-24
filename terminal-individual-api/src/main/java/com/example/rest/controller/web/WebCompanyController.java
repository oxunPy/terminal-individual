package com.example.rest.controller.web;

import com.example.rest.constant.ConstEndpoints;
import com.example.rest.model.form.company.CompanyForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.service.SettingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Locale;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebCompanyController {

    private final SettingService settingService;

    @Value("${upload.folder.img}")
    private String IMG_FOLDER;

    @GetMapping(ConstEndpoints.URL_AJAX_PAGE_COMPANY)
    @Operation(hidden = true)
    public String page(){
        return "/company/view";
    }

    @ResponseBody
    @PostMapping(ConstEndpoints.URL_AJAX_COMPANIES)
    @Operation(hidden = true)
    public DataTablesForm<CompanyForm> getCompaniesDataTable(@RequestBody FilterForm filter, HttpServletRequest request){
        return settingService.getCompanyDataTable(filter, request);
    }

    @GetMapping(ConstEndpoints.URL_AJAX_COMPANY_IMG)
    @Operation(hidden = true)
    public void img(HttpServletResponse response,
                    @PathVariable(value = "filename") String filename) throws FileNotFoundException {

        File folder = new File(System.getProperty("user.home") + IMG_FOLDER);
        if (!folder.exists()){
            folder.mkdirs();
        }
        File file = new File(folder, URLDecoder.decode(filename));
        if(!file.exists()) throw new FileNotFoundException();

        /*Finding MIME type for explicitly setting MIME */
        String mimeType = new MimetypesFileTypeMap().getContentType(file);
        response.setContentType(mimeType);

        /*Browser tries to open it*/
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);

        try{
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(ConstEndpoints.URL_AJAX_COMPANY_SAVE)
    @Operation(hidden = true)
    public ResponseEntity<Object> saveCompany(Locale locale,
                                              @ModelAttribute CompanyForm companyForm){

        return ResponseEntity.ok(settingService.saveCompany(companyForm));
    }

    @PostMapping(ConstEndpoints.URL_AJAX_COMPANY_LOGO_SAVE)
    @Operation(hidden = true)
    public ResponseEntity<Object> saveLogo(HttpServletRequest request,
                                     @RequestParam(value = "companyId", required = false) Long companyId,
                                     @RequestParam(value = "file") MultipartFile multipartFile){
        return ResponseEntity.ok(settingService.saveCompanyLogoAndGetURI(request, companyId, multipartFile));
    }

    @PostMapping(ConstEndpoints.URL_AJAX_COMPANY_FAVICON_SAVE)
    @Operation(hidden = true)
    public ResponseEntity<String> saveFavicon(HttpServletRequest request,
                                              @RequestParam(value = "companyId", required = false) Long companyId,
                                              @RequestParam(value = "file") MultipartFile multipartFile){
        return ResponseEntity.ok(settingService.saveCompanyFaviconAndGetURI(request, companyId, multipartFile));
    }

    @PostMapping(ConstEndpoints.URL_AJAX_EDIT_COMPANY)
    @Operation(hidden = true)
    public ResponseEntity<Object> editCompany(Locale locale,
                                              @ModelAttribute CompanyForm companyForm){
        return ResponseEntity.ok(settingService.editCompany(companyForm));
    }

    @DeleteMapping(ConstEndpoints.URL_AJAX_DELETE_COMPANY)
    @Operation(hidden = true)
    public ResponseEntity<Object> deleteCompany(Locale locale, @PathVariable("id") Long companyId){
        return ResponseEntity.ok(settingService.deleteCompany(companyId));
    }

    @GetMapping(ConstEndpoints.URL_AJAX_COMPANY_GET)
    @Operation(hidden = true)
    public ResponseEntity<Object> getCompany(Locale locale, HttpServletRequest request, @PathVariable("id") Long companyId){
        return ResponseEntity.ok(settingService.getCompanyById(request, companyId));
    }


    @GetMapping(ConstEndpoints.URL_AJAX_COMPANY_LOGO)
    @Operation(hidden = true)
    public void getCompanyLogo(HttpServletResponse response) throws FileNotFoundException {

        String logoFileName = settingService.getMainCompanyLogoName();

        File folder = new File(System.getProperty("user.home") + IMG_FOLDER);
        if (!folder.exists()){
            folder.mkdirs();
        }

        File file = null;
        if(StringUtils.isBlank(logoFileName)) file = new File("classpath:/static/media/window_and_door.svg");
        else file = new File(folder, URLDecoder.decode(logoFileName));
        if(!file.exists()) throw new FileNotFoundException();

        /*Finding MIME type for explicitly setting MIME */
        String mimeType = new MimetypesFileTypeMap().getContentType(file);
        response.setContentType(mimeType);

        /*Browser tries to open it*/
        response.addHeader("Content-Disposition", "attachment;filename=" + logoFileName);

        try{
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(ConstEndpoints.URL_AJAX_COMPANY_FAVICON)
    @Operation(hidden = true)
    public void getCompanyFavicon(HttpServletResponse response) throws FileNotFoundException {

        String faviconFileName = settingService.getMainCompanyFaviconName();

        File folder = new File(System.getProperty("user.home") + IMG_FOLDER);
        if (!folder.exists()){
            folder.mkdirs();
        }
        File file = null;
        if(!StringUtils.isBlank(faviconFileName)) file = new File(folder, URLDecoder.decode(faviconFileName));
        else file = new File("classpath:/static/media/favicons/favicon.ico");
        if(!file.exists()) throw new FileNotFoundException();

        /*Finding MIME type for explicitly setting MIME */
        String mimeType = new MimetypesFileTypeMap().getContentType(file);
        response.setContentType(mimeType);

        /*Browser tries to open it*/
        response.addHeader("Content-Disposition", "attachment;filename=" + faviconFileName);

        try{
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
