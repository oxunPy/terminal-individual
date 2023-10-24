package com.example.rest.dto;

import com.example.rest.dto.dtoProjection.WareHouseDtoProjection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class WareHouseDto {
    private Long id;

    private String name;

    private Boolean isDefault;

    public static WareHouseDto buildFromDtoProjection(@NotNull WareHouseDtoProjection dtoProjection){
        WareHouseDto dto = new WareHouseDto();
        BeanUtils.copyProperties(dtoProjection, dto);
        return dto;
    }
}
