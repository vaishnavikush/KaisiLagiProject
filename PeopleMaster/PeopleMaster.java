package com.example.kaisi_lagi.PeopleMaster;

import com.example.kaisi_lagi.MovieCast.MovieCast;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "people_master")
public class PeopleMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "people_id")
    private Long pid;
    @Column(name = "people_name",nullable = true)
    private String peopleName;
    @Column(name = "role")
    private String role;
    private int points;
    private byte[] image;
    @Column(columnDefinition = "TEXT")
    private String people_bio;
    private LocalDate people_dob;
    @Column(columnDefinition = "TEXT")
    private String people_debut;
    @Column(columnDefinition = "TEXT")
    private String people_awards;
    private int debut_date;


    @OneToMany(mappedBy = "people",cascade = CascadeType.ALL)
    private List<MovieCast> movieCasts;

    public String getPeople_bio() {
        return people_bio;
    }

    public void setPeople_bio(String people_bio) {
        this.people_bio = people_bio;
    }

    public PeopleMaster() {
    }


    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }


    public  String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<MovieCast> getMovieCasts() {
        return movieCasts;
    }

    public void setMovieCasts(List<MovieCast> movieCasts) {
        this.movieCasts = movieCasts;
    }

    public LocalDate getPeople_dob() {
        return people_dob;
    }

    public void setPeople_dob(LocalDate people_dob) {
        this.people_dob = people_dob;
    }

    public String getPeople_debut() {
        return people_debut;
    }

    public void setPeople_debut(String people_debut) {
        this.people_debut = people_debut;
    }

    public String getPeople_awards() {
        return people_awards;
    }

    public void setPeople_awards(String people_awards) {
        this.people_awards = people_awards;
    }

    public int getDebut_date() {
        return debut_date;
    }

    public void setDebut_date(int debut_date) {
        this.debut_date = debut_date;
    }
}