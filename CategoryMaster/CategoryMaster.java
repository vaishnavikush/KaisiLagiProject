package com.example.kaisi_lagi.CategoryMaster;

import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "category_master")
public class CategoryMaster {
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

    public List<MovieMaster> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieMaster> movies) {
        this.movies = movies;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cate_id")
    private Long id;
    @Column(name = "cate_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<MovieMaster> movies;

}
