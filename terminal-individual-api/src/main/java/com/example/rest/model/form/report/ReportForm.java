package com.example.rest.model.form.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReportForm {
    private Long id;



    private Date date;

    private String clientName;

    private String warehouse;
}
