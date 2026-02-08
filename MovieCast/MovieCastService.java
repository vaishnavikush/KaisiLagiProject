package com.example.kaisi_lagi.MovieCast;

import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.PeopleMaster.PeopleMaster;
import com.example.kaisi_lagi.PeopleMaster.PeopleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieCastService {

    @Autowired
    private PeopleRepository peopleRepository;

    @Transactional
    public void updateCastByRole(
            MovieMaster movie,
            String role,
            List<Long> peopleIds
    ) {
        if (peopleIds == null) return;

        // Initialize collection to prevent LazyInitializationException
        movie.getMovieCasts().size();

        // Remove old casts for this role
        movie.getMovieCasts().removeIf(c -> role.equals(c.getRole()));

        // Add new cast
        for (Long pid : peopleIds) {
            PeopleMaster person = peopleRepository.findById(pid)
                    .orElseThrow(() -> new RuntimeException("Person not found"));

            MovieCast cast = new MovieCast();
            cast.setMovie(movie);
            cast.setPeople(person);
            cast.setRole(role);

            movie.getMovieCasts().add(cast);
        }
    }
}
