package com.app.my_project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.my_project.entity.myWebEntity;

public interface myWebRepository extends JpaRepository<myWebEntity, Long> {
    List<myWebEntity> findAllByOrderByIdAsc();

    Page<myWebEntity> findAll(Pageable pageable);

    Page<myWebEntity> findByNameContaining(String keyword, Pageable pageable);

    Page<myWebEntity> findByRegionContaining(String keyword, Pageable pageable);

    Page<myWebEntity> findByElementContaining(String keyword, Pageable pageable);

    Page<myWebEntity> findByGenderContaining(String keyword, Pageable pageable);

    Page<myWebEntity> findByRarityEquals(Integer keyword, Pageable pageable);

    // Slice<myOwnNextjsWebEntity> findAll(Pageable pageable);
    // List<myOwnNextjsWebEntity> findDistinctByRegion(String region);

    // List<myOwnNextjsWebEntity> findByRegion(String region);

    // List<myOwnNextjsWebEntity> findAllByGender(String gender);

    // List<myOwnNextjsWebEntity> findByElementOrderByName(String element);
}
