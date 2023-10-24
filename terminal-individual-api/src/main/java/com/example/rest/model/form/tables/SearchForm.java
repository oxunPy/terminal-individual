package com.example.rest.model.form.tables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchForm implements Serializable {
    private static final long serialVersionUID = -3029620531741939122L;

    private String value;
    private boolean regex;
}
