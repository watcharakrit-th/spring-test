package com.app.my_project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.my_project.entity.myOwnNextjsWebEntity;

public interface myOwnNextjsWebRepository extends JpaRepository<myOwnNextjsWebEntity, Long> {
    List<myOwnNextjsWebEntity> findAllByOrderByIdAsc();

    Page<myOwnNextjsWebEntity> findAll(Pageable pageable);

    Page<myOwnNextjsWebEntity> findByNameContaining(String keyword, Pageable pageable);

    Page<myOwnNextjsWebEntity> findByRegionContaining(String keyword, Pageable pageable);

    Page<myOwnNextjsWebEntity> findByElementContaining(String keyword, Pageable pageable);

    Page<myOwnNextjsWebEntity> findByGenderContaining(String keyword, Pageable pageable);

    Page<myOwnNextjsWebEntity> findByRarityEquals(Integer keyword, Pageable pageable);

    // Slice<myOwnNextjsWebEntity> findAll(Pageable pageable);
    // List<myOwnNextjsWebEntity> findDistinctByRegion(String region);

    // List<myOwnNextjsWebEntity> findByRegion(String region);

    // List<myOwnNextjsWebEntity> findAllByGender(String gender);

    // List<myOwnNextjsWebEntity> findByElementOrderByName(String element);
}
