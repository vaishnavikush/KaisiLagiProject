package com.example.kaisi_lagi.ImageMaster;

import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageMaster,Long> {
    ImageMaster findFirstByMovieMovieId(Long movieId);
    List<ImageMaster> findByMovie(MovieMaster movie);

    void deleteByMovieMovieId(Long movieId);
    @Query("SELECT i FROM ImageMaster i WHERE i.movie = :movie AND i.url IS NOT NULL")
    Optional<ImageMaster> findTrailerByMovie(@Param("movie") MovieMaster movie);

    Optional<ImageMaster> findFirstByMovieAndUrlIsNotNull(MovieMaster mv);


}
