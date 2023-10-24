package com.example.rest.model.form.tables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderForm implements Serializable {

    private static final long serialVersionUID = -2069131671088646133L;

    private int column;
    private String dir;
}
