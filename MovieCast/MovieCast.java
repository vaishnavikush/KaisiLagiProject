package com.example.kaisi_lagi.MovieCast;

import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.PeopleMaster.PeopleMaster;
import jakarta.persistence.*;

@Entity
@Table(name = "movie_cast")
public class MovieCast {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovieMaster getMovie() {
        return movie;
    }

    public void setMovie(MovieMaster movie) {
        this.movie = movie;
    }

    public PeopleMaster getPeople() {
        return people;
    }

    public void setPeople(PeopleMaster people) {
        this.people = people;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cast_id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "movie_id",nullable = false)
    private MovieMaster movie;

    @ManyToOne()
    @JoinColumn(name = "people_id",nullable = false)
    private PeopleMaster people;

    @Column(name = "role")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}

