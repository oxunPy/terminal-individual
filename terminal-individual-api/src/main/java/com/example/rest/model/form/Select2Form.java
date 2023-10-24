package com.example.rest.model.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Select2Form {
    private Long id;
    private String text;
    private List<Select2Form> children;

    public Select2Form(Long id, String text) {
        this.id = id;
        this.text = text;
    }
}
