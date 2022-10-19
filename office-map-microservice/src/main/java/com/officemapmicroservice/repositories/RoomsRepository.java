package com.officemapmicroservice.repositories;

import com.officemapmicroservice.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomsRepository extends JpaRepository<Room, Long> {
}
