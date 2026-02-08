package com.example.kaisi_lagi.ReviewMaster;

import com.example.kaisi_lagi.CategoryMaster.CategoryMaster;
import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.PeopleMaster.PeopleMaster;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewMaster, Long> {

    Long countByMovie(MovieMaster movieMaster);

    List<ReviewMaster> findByUserAndMovie(UserMaster user, MovieMaster movie);

    List<ReviewMaster> findByMovie(MovieMaster movie);

    boolean existsByUser(UserMaster userMaster);

    List<ReviewMaster> findAllByUser(UserMaster userMaster);

    List<ReviewMaster> findByUser(UserMaster user);

    Optional<ReviewMaster> findByUserAndMovieAndPeopleIsNull(UserMaster user, MovieMaster movie);

    List<ReviewMaster> findByPeople_Pid(Long pid);

    List<ReviewMaster> findByMovieAndUser(MovieMaster movie, UserMaster user);

    @Query("""
        SELECT AVG(r.rating)
        FROM ReviewMaster r
        WHERE r.people.pid = :pid
        AND r.people IS NOT NULL
    """)
    Double getAvgRatingByPeople(@Param("pid") Long pid);

    @Query("""
        SELECT AVG(r.rating)
        FROM ReviewMaster r
        WHERE r.movie.movieId = :mid
        AND r.people IS NULL
    """)
    Double getAvgRatingByMovie(@Param("mid") Long mid);

    @Query("""
        SELECT COUNT(r)
        FROM ReviewMaster r
        WHERE r.people.pid = :pid
        AND r.people IS NOT NULL
    """)
    Long getVoteCountByPeople(@Param("pid") Long pid);

    @Query("""
        SELECT COUNT(r)
        FROM ReviewMaster r
        WHERE r.movie.movieId = :movieId
        AND r.people IS NULL
    """)
    Long getVoteCountByMovie(@Param("movieId") Long movieId);

    long countByUserAndCategory(UserMaster user, CategoryMaster category);

    @Query(value = """
        SELECT m.movie_name, ROUND(AVG(r.rating), 1) AS avg_rating
        FROM movie_master m
        JOIN review_master r ON m.movie_id = r.movie_id
        JOIN category_master c ON m.cate_id = c.cate_id
        WHERE UPPER(c.cate_name) = 'MOVIE'
        AND r.people_id IS NULL
        GROUP BY m.movie_name
        HAVING COUNT(r.review_id) > 0
        ORDER BY avg_rating DESC
        LIMIT 1
        """, nativeQuery = true)
    List<Object[]> getTopMovie();

    @Query(value = """
        SELECT m.movie_name, ROUND(AVG(r.rating), 1) AS avg_rating
        FROM movie_master m
        JOIN review_master r ON m.movie_id = r.movie_id
        JOIN category_master c ON m.cate_id = c.cate_id
        WHERE UPPER(c.cate_name) = 'WEB SERIES'
        AND r.people_id IS NULL
        GROUP BY m.movie_name
        HAVING COUNT(r.review_id) > 0
        ORDER BY avg_rating DESC
        LIMIT 1
        """, nativeQuery = true)
    List<Object[]> getTopWebseries();

    @Query(value = """
        SELECT m.movie_name, ROUND(AVG(r.rating), 1) AS avg_rating
        FROM movie_master m
        JOIN review_master r ON m.movie_id = r.movie_id
        JOIN category_master c ON m.cate_id = c.cate_id
        WHERE UPPER(c.cate_name) = 'TV SHOW'
        AND r.people_id IS NULL
        GROUP BY m.movie_name
        HAVING COUNT(r.review_id) > 0
        ORDER BY avg_rating DESC
        LIMIT 1
        """, nativeQuery = true)
    List<Object[]> getTopTvShow();

    @Query(value = """
        SELECT DATE(review_date) AS day, COUNT(*) AS count
        FROM review_master
        WHERE review_date >= CURRENT_DATE - INTERVAL '7 days'
        GROUP BY DATE(review_date)
        ORDER BY day ASC
        """, nativeQuery = true)
    List<Object[]> getDailyReviews();

    @Query("""
        SELECT r FROM ReviewMaster r
        WHERE r.movie.movieId = :movieId
        AND r.user.id = :userId
        AND r.people IS NULL
    """)
    Optional<ReviewMaster> findUserMovieReview(@Param("movieId") Long movieId, @Param("userId") Long userId);

    @Query("""
        SELECT r FROM ReviewMaster r
        WHERE r.movie.movieId = :movieId
        AND r.user.id = :userId
        AND r.people.pid = :pid
    """)
    Optional<ReviewMaster> findUserPeopleReview(@Param("movieId") Long movieId, @Param("userId") Long userId, @Param("pid") Long pid);

    @Query("""
        SELECT r FROM ReviewMaster r
        WHERE r.movie.movieId = :movieId
        AND r.people IS NULL
        ORDER BY r.reviewDate DESC
    """)
    List<ReviewMaster> findMovieReviews(@Param("movieId") Long movieId);

    @Query("""
        SELECT r FROM ReviewMaster r
        WHERE r.people.pid = :pid
        AND r.people IS NOT NULL
        ORDER BY r.reviewDate DESC
    """)
    List<ReviewMaster> findPeopleReviews(@Param("pid") Long pid);
}