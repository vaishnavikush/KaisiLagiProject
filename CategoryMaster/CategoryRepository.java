package com.example.kaisi_lagi.CategoryMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryMaster, Long> {
    long countByName(String name);
   // CategoryMaster findBySubcate(CategoryMaster id);
    CategoryMaster findByName(String name);
}
