package com.example.kaisi_lagi;

import com.example.kaisi_lagi.CategoryMaster.CategoryMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {

    List<SubCategory> findByCategory(CategoryMaster id);

}
