package com.officemap.mapper;

import com.officemap.dto.ComponentDto;
import com.officemap.units.Component;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

import static com.officemap.enumeration.ComponentsEnum.COUNTRY;
import static org.junit.jupiter.api.Assertions.*;

class ComponentMapperTest {
    @Autowired
    private ComponentMapper componentMapper = new ComponentMapper();

    @Test
    void testComponentEntityToDto() {
        Component componentCountry = new Component(1L, COUNTRY, "Bulgaria", 0L, "Language: Bulgarian");
        ComponentDto componentDto = componentMapper.componentEntityToDto(componentCountry);
        assertAll(
                () -> assertTrue(componentDto.getId().equals(componentCountry.getId())),
                () -> assertTrue(componentDto.getComponentType().toUpperCase(Locale.ROOT).equals(componentCountry.getComponentEnum().getComponentType().toUpperCase(Locale.ROOT))),
                () -> assertTrue(componentDto.getName().equals(componentCountry.getName())),
                () -> assertTrue(componentDto.getParentId().equals(componentCountry.getParentId())),
                () -> assertTrue(componentDto.getDetails().equals(componentCountry.getDetails()))
        );
    }
}