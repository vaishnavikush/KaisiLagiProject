package com.example.kaisi_lagi.MovieMaster;

import com.example.kaisi_lagi.CategoryMaster.CategoryMaster;
import com.example.kaisi_lagi.ImageMaster.ImageMaster;
import com.example.kaisi_lagi.MovieCast.MovieCast;
import com.example.kaisi_lagi.ReviewMaster.ReviewMaster;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "movie_master")
public class MovieMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "movie_name", nullable = false, length = 150)
    private String movieName;

    private String language;
    private String genre;



    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private String duration;

    @ManyToOne
    @JoinColumn(name = "cate_id")
    private CategoryMaster category;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<MovieCast> movieCasts;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<ImageMaster> images;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<ReviewMaster> reviews;

    // Getters and Setters
    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public CategoryMaster getCategory() { return category; }
    public void setCategory(CategoryMaster category) { this.category = category; }

    public List<MovieCast> getMovieCasts() { return movieCasts; }
    public void setMovieCasts(List<MovieCast> movieCasts) { this.movieCasts = movieCasts; }

    public List<ImageMaster> getImages() { return images; }
    public void setImages(List<ImageMaster> images) { this.images = images; }

    public List<ReviewMaster> getReviews() { return reviews; }
    public void setReviews(List<ReviewMaster> reviews) { this.reviews = reviews; }
}
