package com.example.kaisi_lagi.PeopleMaster;

import com.example.kaisi_lagi.MovieCast.MovieCast;
import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PeopleRepository extends JpaRepository<PeopleMaster,Long> {
    List<PeopleMaster> findByRoleAndPeopleNameContainingIgnoreCase(String role,String peopleName);


    @Query("select pm from PeopleMaster pm where LOWER(pm.peopleName) LIKE LOWER(CONCAT(:peopleName, '%')) OR " +
            "LOWER(pm.role) LIKE LOWER(CONCAT(:role, '%')) ")
    List<PeopleMaster>findPeopleByRoleAndName(@Param("peopleName") String peopleName,@Param("role") String role);

    @Query("SELECT pn FROM PeopleMaster pn WHERE LOWER(pn.peopleName) LIKE LOWER(CONCAT(:peopleName, '%'))")
    List<PeopleMaster> findMovieByOnType(@Param("peopleName") String peopleName);

    List<PeopleMaster>findByRoleIgnoreCase(String role);
    List<PeopleMaster>findByPeopleNameContainingIgnoreCase(String peopleName);

    //    @Query(value = "SELECT DISTINCT people_name FROM people_master ORDER BY people_name", nativeQuery = true)
//    List<String> findAllDistinctPeopleName();
//
//    @Query("SELECT DISTINCT p.peopleName, p.pid FROM PeopleMaster p")
//    List<PeopleMaster> findAllDistinctPeople();
    @Query("SELECT mc FROM MovieCast mc WHERE mc.movie.movieId = :movieId")
    List<MovieCast> findCastsByMovie(@Param("movieId") Long movieId);
    Optional<PeopleMaster> findByPeopleName(String peopleName);

    @Query(value = """
    SELECT p.* 
    FROM people_master p
    WHERE p.role ILIKE 'Actor' OR p.role ILIKE 'Actress'
    ORDER BY p.points DESC
    LIMIT :limit
""", nativeQuery = true)
    List<PeopleMaster> findTopActors(@Param("limit") int limit);

    // Optional: fetch people with movies
    @Query("SELECT p FROM PeopleMaster p LEFT JOIN FETCH p.movieCasts mc LEFT JOIN FETCH mc.movie WHERE p.pid = :peopleId")
    Optional<PeopleMaster> findByIdWithMovies(@Param("peopleId") Long peopleId);
    @Query("""
SELECT p, COALESCE(ROUND(AVG(r.rating), 1), 0)
FROM PeopleMaster p
JOIN MovieCast mc ON mc.people.pid = p.pid
JOIN ReviewMaster r ON r.movie.movieId = mc.movie.movieId
WHERE LOWER(p.role) = LOWER(:role)
GROUP BY p
ORDER BY AVG(r.rating) DESC
""")
    List<Object[]> findTopPeopleByRole(
            @Param("role") String role,
            Pageable pageable
    );


}