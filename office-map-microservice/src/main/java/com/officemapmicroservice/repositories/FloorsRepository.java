package com.officemapmicroservice.repositories;

import com.officemapmicroservice.entities.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorsRepository extends JpaRepository<Floor, Long> {
}
