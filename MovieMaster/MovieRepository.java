package com.example.kaisi_lagi.MovieMaster;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieMaster, Long> {
    List<MovieMaster> findByMovieNameIgnoreCase(String movieName);
    List<MovieMaster> findByMovieNameContainingIgnoreCase(String movieName);

    @Query("SELECT mn FROM MovieMaster mn WHERE LOWER(mn.movieName) LIKE LOWER(CONCAT(:movieName, '%'))")
    List<MovieMaster> findMovieByOnType(@Param("movieName") String movieName);

    // FIX: Use the CategoryMaster name field
    long countByCategory_Name(String name);
   // long countByCategory_NameIgnoreCase(String name);

    // ================= HERO / RECENT MOVIES =================
    List<MovieMaster> findTop10ByOrderByMovieIdDesc();

    // ================= TOP MOVIES / WEB SERIES / TV SHOW BY RATING =================

//    @Query("""
//    SELECT DISTINCT m FROM MovieMaster m
//    WHERE
//        (
//            :q IS NOT NULL AND
//            (
//                LOWER(m.movieName) LIKE :q OR
//                LOWER(m.genre) LIKE :q OR
//                LOWER(m.language) LIKE :q
//            )
//        )
//        OR
//        (
//            :q IS NULL AND
//            (:category IS NULL OR LOWER(m.category.name) = :category)
//        )
//""")
//    Page<MovieMaster> searchAllFields(
//            @Param("q") String q,
//            @Param("category") String category,
//            Pageable pageable
//    );




    @Query("SELECT DISTINCT m.movieName FROM MovieMaster m " +
            "WHERE LOWER(m.movieName) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(m.genre) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(m.language) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<String> searchAutocomplete(@Param("term") String term);



    long countByCategory_NameIgnoreCase(String movies);
    // ================= FETCH MOVIE WITH IMAGES AND CAST =================
    @Query("""
        SELECT m
        FROM MovieMaster m
        LEFT JOIN FETCH m.images
        LEFT JOIN FETCH m.movieCasts mc
        LEFT JOIN FETCH mc.people
        WHERE m.movieId = :movieId
    """)
    Optional<MovieMaster> findByIdWithImagesAndCast(@Param("movieId") Long movieId);
    //    @Query("""
//SELECT m
//FROM MovieMaster m
//JOIN m.reviews r
//JOIN m.category c
//WHERE UPPER(c.cateName) = :category
//GROUP BY m.movieId
//ORDER BY AVG(r.rating) DESC
//""")
//    List<MovieMaster> findTopByCategoryOrderByAvgRatingDesc(
//            @Param("category") String category,
//            Pageable pageable
//    );
    @Query(value = """
    SELECT 
        m.movie_id,
        m.movie_name,
        ROUND(COALESCE(AVG(r.rating), 0), 1) AS avg_rating
    FROM movie_master m
    LEFT JOIN review_master r
           ON m.movie_id = r.movie_id
          AND r.people_id IS NULL
    JOIN category_master c
           ON m.cate_id = c.cate_id
    WHERE UPPER(c.cate_name) = UPPER(:category)
    GROUP BY m.movie_id, m.movie_name
    ORDER BY avg_rating DESC
    LIMIT 10
    """, nativeQuery = true)
    List<Object[]> getTop10ByCategory(@Param("category") String category);

//    Page<MovieMaster> fallbackSearch(
//            @Param("letter") String letter,
//            Pageable pageable
//    );
    @Query("""
    SELECT DISTINCT m
    FROM MovieMaster m
    LEFT JOIN m.category c
    WHERE
        (:q IS NULL OR
            m.movieName ILIKE :q OR
            m.genre ILIKE :q OR
            m.language ILIKE :q
        )
    AND
        (:category IS NULL OR c.name ILIKE :category)
""")
    Page<MovieMaster> searchAllFields(
            @Param("q") String q,
            @Param("category") String category,
            Pageable pageable
    );


    @Query("""
        SELECT m
        FROM MovieMaster m
        WHERE LOWER(m.movieName) LIKE LOWER(CONCAT('%', :letter, '%'))
    """)
    Page<MovieMaster> fallbackSearch(
            @Param("letter") String letter,
            Pageable pageable
    );
}
