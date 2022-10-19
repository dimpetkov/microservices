package com.officemapmicroservice.repositories;

import com.officemapmicroservice.entities.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingsRepository extends JpaRepository<Building, Long> {
}
