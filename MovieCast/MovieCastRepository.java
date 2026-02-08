package com.example.kaisi_lagi.MovieCast;

import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.PeopleMaster.PeopleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieCastRepository extends JpaRepository<MovieCast,Long> {

    List<MovieCast> findAllByMovie(MovieMaster movieMaster);
    boolean existsByMovieAndPeople(MovieMaster movie, PeopleMaster person);

    void deleteByMovie_MovieIdAndRole(Long movieId, String role);

   // List<MovieCast> findAllByMovie(MovieMaster movieMaster);
    List<MovieCast> findAllByPeople(PeopleMaster peopleMaster);
    List<MovieCast> findByMovie(MovieMaster movie);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM MovieCast mc WHERE mc.movie = :movie AND mc.role = :role")
    void deleteByMovieAndRole(MovieMaster movie, String role);


    @Query("SELECT mc FROM MovieCast mc WHERE mc.movie.movieId = :movieId")
    List<MovieCast> findCastsByMovie(@Param("movieId") Long movieId);
}

