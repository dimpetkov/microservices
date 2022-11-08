package com.officemap.repository;

import com.officemap.units.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {
    /** ToCheck if works */
    @Query(value = "SELECT * FROM components WHERE components_enum = ?1", nativeQuery = true)
    List<Component> findAllByComponentsEnum(int componentsEnum);

    @Query(value = "SELECT * FROM components WHERE parent_id = ?1", nativeQuery = true)
    List<Component> findAllByParentId(Long parentId);

}
