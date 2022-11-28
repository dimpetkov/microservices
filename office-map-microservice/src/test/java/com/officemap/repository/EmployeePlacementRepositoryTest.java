package com.officemap.repository;

import com.officemap.units.EmployeePlacement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class EmployeePlacementRepositoryTest {

    @Autowired
    private EmployeePlacementRepository underTestRepository;

    @BeforeEach
    public void setUp() {
        EmployeePlacement employeePlacementOne = new EmployeePlacement(1L, 2L);
        EmployeePlacement employeePlacementTwo = new EmployeePlacement(5L, 3L);
        underTestRepository.save(employeePlacementOne);
        underTestRepository.save(employeePlacementTwo);
    }

    @AfterEach
    public void tearDown() {
        underTestRepository.deleteAll();
    }

    @Test
    void testFindByDeskIdReturnTrue() {
        EmployeePlacement findEmployeePlacementByDeskId;
        Long deskIdForTest = 2L;
        findEmployeePlacementByDeskId = underTestRepository.findByDeskId(deskIdForTest);
        assertNotNull(findEmployeePlacementByDeskId);
        assertEquals(1L, findEmployeePlacementByDeskId.getEmployeeId());
    }

    @Test
    void testFindByDeskIdReturnFalse() {
        EmployeePlacement findEmployeePlacementByDeskId;
        Long deskIdForTest = 10L;
        findEmployeePlacementByDeskId = underTestRepository.findByDeskId(deskIdForTest);
        assertNull(findEmployeePlacementByDeskId);
    }

    @Test
    void testFindByEmployeeIdReturnTrue() {
        List<EmployeePlacement> findEmployeePlacementByEmployeeId;
        Long employeeIdForTest = 5L;
        findEmployeePlacementByEmployeeId = underTestRepository.findByEmployeeId(employeeIdForTest);
        assertFalse(findEmployeePlacementByEmployeeId.isEmpty());
        assertEquals(3L, findEmployeePlacementByEmployeeId.stream().findFirst().get().getDeskId());
    }

    @Test
    void testFindByEmployeeIdReturnFalse() {
        List<EmployeePlacement> findEmployeePlacementByEmployeeId;
        Long employeeIdForTest = 20L;
        findEmployeePlacementByEmployeeId = underTestRepository.findByEmployeeId(employeeIdForTest);
        assertTrue(findEmployeePlacementByEmployeeId.isEmpty());
    }

}