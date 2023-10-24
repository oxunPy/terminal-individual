package com.example.rest.model.form.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CompanyForm {

    private Long id;

    private String companyName;

    private String faviconUrl;

    private String logoImgUrl;

    private String manager;

    private String director;

    private String email;

    private Boolean isMain;

    private String motto;

    private String phoneNumber;

    private String telegramContact;

    private MultipartFile favicon;

    private MultipartFile logo;
}
