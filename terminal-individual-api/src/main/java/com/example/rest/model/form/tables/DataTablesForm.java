package com.example.rest.model.form.tables;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataTablesForm<T> implements Serializable {
    private static final long serialVersionUID = 4567927313238036854L;

    public int draw;
    public int recordsTotal;
    public int recordsFiltered;
    private String error;
    public List<T> data;
}
