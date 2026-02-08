package com.example.kaisi_lagi;

import com.example.kaisi_lagi.CategoryMaster.CategoryMaster;
import jakarta.persistence.*;


@Entity
@Table(name="SubCategoryMaster")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryMaster category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryMaster getCategory() {
        return category;
    }

    public void setCategory(CategoryMaster category) {
        this.category = category;
    }

    public SubCategory(Long id, String name, CategoryMaster category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public SubCategory() {
    }
}
