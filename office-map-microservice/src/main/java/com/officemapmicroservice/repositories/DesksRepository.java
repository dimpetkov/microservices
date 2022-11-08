package com.officemapmicroservice.repositories;

import com.officemapmicroservice.entities.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesksRepository extends JpaRepository<Desk, Long> {
}
