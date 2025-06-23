package com.gll.blog.repositories;

import com.gll.blog.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    boolean existsByName(String name);

    Optional<CategoryEntity> findByName(String name);
}
