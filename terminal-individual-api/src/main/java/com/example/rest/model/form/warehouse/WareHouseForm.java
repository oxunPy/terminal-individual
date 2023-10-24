package com.example.rest.model.form.warehouse;

import com.example.rest.dto.WareHouseDto;
import com.example.rest.dto.dtoProjection.WareHouseDtoProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class WareHouseForm {
    private Long id;

    private String name;

    private Boolean isDefault;

    public static WareHouseDto buildFromDtoProjection(@NotNull WareHouseDtoProjection dtoProjection){
        WareHouseDto dto = new WareHouseDto();
        BeanUtils.copyProperties(dtoProjection, dto);
        return dto;
    }
}
