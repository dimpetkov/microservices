package com.officemap.mapper;

import com.officemap.units.Component;
import com.officemap.dto.ComponentDto;
import org.springframework.stereotype.Service;

@Service
public class ComponentMapper {

    public ComponentDto componentEntityToDto(Component componentEntity) {
        ComponentDto componentDto = new ComponentDto();
        componentDto.setId(componentEntity.getId());
        componentDto.setComponentType(componentEntity.getComponentEnum().getComponentType());
        componentDto.setName(componentEntity.getName());
        componentDto.setParentId(componentEntity.getParentId());
        componentDto.setDetails(componentEntity.getDetails());
        return componentDto;
    }
}
