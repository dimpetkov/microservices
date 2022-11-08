package com.officemap.repository;

import com.officemap.units.EmployeePlacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//@Repository
public interface EmployeePlacementRepository extends JpaRepository<EmployeePlacement, Long> {

    @Query(value = "SELECT * FROM placements WHERE desk_id = ?1", nativeQuery = true)
    EmployeePlacement findByDeskId(Long deskId);

    @Query(value = "SELECT * FROM placements WHERE employee_id = ?1", nativeQuery = true)
    List<EmployeePlacement> findByEmployeeId(Long employeeId);

    @Modifying
    @Query(value = "DELETE FROM placements WHERE deskId=?1", nativeQuery = true)
    void deleteByDeskId(Long deskId);
}
