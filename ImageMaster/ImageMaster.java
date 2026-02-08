package com.example.kaisi_lagi.ImageMaster;

import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import jakarta.persistence.*;

@Entity
@Table(name = "image_master")
public class ImageMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;
    @Column(name = "images")
    private byte[] movieImages;
    private String url;

    @ManyToOne()
    @JoinColumn(name = "movie_id")
    private MovieMaster movie;

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public byte[] getMovieImages() {
        return movieImages;
    }

    public void setMovieImages(byte[] movieImages) {
        this.movieImages = movieImages;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MovieMaster getMovie() {
        return movie;
    }

    public void setMovie(MovieMaster movie) {
        this.movie = movie;
    }
}
