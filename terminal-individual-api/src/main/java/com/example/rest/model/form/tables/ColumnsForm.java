package com.example.rest.model.form.tables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnsForm implements Serializable {
    private static final long serialVersionUID = -546384123348787675L;

    private String data;
    private String name;
    private boolean searchable;
    private boolean orderable;
    private SearchForm searchForm;
}
