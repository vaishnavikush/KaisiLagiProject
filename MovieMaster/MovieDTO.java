package com.example.kaisi_lagi.MovieMaster;

import com.example.kaisi_lagi.ImageMaster.ImageMaster;

import java.time.LocalDate;
import java.util.List;

public class MovieDTO {

    private Long movieId;
    private String movieName;
    private String genre;
    private String duration;
    private String description;
    private LocalDate releaseDate;
    private List<Long> imageIds;

    private Double avgRating;
    private String posterUrl;
    private String trailerUrl;
    private List<String> castNames;

    // ===== CONSTRUCTORS =====
    public MovieDTO(MovieMaster movie) {
        mapBasic(movie);
        this.avgRating = null;
    }

    public MovieDTO(MovieMaster movie, Double avgRating) {
        mapBasic(movie);
        this.avgRating = avgRating;
    }

    // ===== MAP BASIC DETAILS =====
    private void mapBasic(MovieMaster movie) {
        this.movieId = movie.getMovieId();
        this.movieName = movie.getMovieName();
        this.genre = movie.getGenre();
        this.duration = movie.getDuration();
        this.description = movie.getDescription();
        this.releaseDate = movie.getReleaseDate();

        // Image IDs
        this.imageIds = movie.getImages() == null
                ? List.of()
                : movie.getImages().stream()
                .map(ImageMaster::getImageId)
                .toList();

        // Poster
        if (movie.getImages() != null && !movie.getImages().isEmpty()) {
            ImageMaster img = movie.getImages().get(0);
            if (img.getMovieImages() != null) {
                this.posterUrl = java.util.Base64.getEncoder()
                        .encodeToString(img.getMovieImages());
            }
        }
    }

    // ===== GETTERS =====
    public Long getMovieId() { return movieId; }
    public String getMovieName() { return movieName; }
    public String getGenre() { return genre; }
    public String getDuration() { return duration; }
    public String getDescription() { return description; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public Double getAvgRating() { return avgRating; }
    public String getPosterUrl() { return posterUrl; }
    public String getTrailerUrl() { return trailerUrl; }
    public List<String> getCastNames() { return castNames; }
    public List<Long> getImageIds() { return imageIds; }

    // ===== SETTERS =====
    public void setAvgRating(Double avgRating) { this.avgRating = avgRating; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }
    public void setCastNames(List<String> castNames) { this.castNames = castNames; }
}
