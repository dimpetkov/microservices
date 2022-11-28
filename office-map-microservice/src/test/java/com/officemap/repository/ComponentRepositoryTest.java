package com.officemap.repository;

import com.officemap.units.Component;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.officemap.enumeration.ComponentsEnum.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class ComponentRepositoryTest {

    @Autowired
    private ComponentRepository underTestRepository;

    @BeforeEach
    public void setUp() {
        Component componentCountry = new Component(1L, COUNTRY, "Bulgaria", 0L, "Language: Bulgarian");
        Component componentCitySofia = new Component(2L, CITY, "Sofia", 1L, "Capital");
        Component componentCityBurgas = new Component(3L, CITY, "Burgas", 1L, "sea-side");
        underTestRepository.save(componentCountry);
        underTestRepository.save(componentCitySofia);
        underTestRepository.save(componentCityBurgas);
    }

    @AfterEach
    public void tearDown() {
        underTestRepository.deleteAll();
    }

    @Test
    void testFindAllByComponentsEnumReturnTrue() {
        List<Component> componentsByType = underTestRepository.findAllByComponentsEnum(COUNTRY.ordinal());
        assertEquals(1, componentsByType.size());
        assertEquals("Bulgaria", componentsByType.stream().findFirst().get().getName());
    }

    @Test
    void testFindAllByComponentsEnumReturnFalse() {
        List<Component> componentsByType = underTestRepository.findAllByComponentsEnum(DESK.ordinal());
        assertTrue(componentsByType.isEmpty());
    }

    @Test
    void testFindAllByParentIdReturnTrue() {
        List<Component> componentsByParentId = underTestRepository.findAllByParentId(1L);
        assertEquals(2, componentsByParentId.size());
    }

    @Test
    void testFindAllByParentIdReturnFalse() {
        List<Component> componentsByParentId = underTestRepository.findAllByParentId(5L);
        assertTrue(componentsByParentId.isEmpty());
    }
}