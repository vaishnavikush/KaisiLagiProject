package com.example.kaisi_lagi.MovieMaster;

import com.example.kaisi_lagi.ImageMaster.ImageRepository;
import com.example.kaisi_lagi.PeopleMaster.PeopleDTO;
import com.example.kaisi_lagi.PeopleMaster.PeopleMaster;
import com.example.kaisi_lagi.PeopleMaster.PeopleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;
import java.util.List;

@Controller
@Transactional
public class HomeController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private ImageRepository imageRepository;

    // ================= HOME PAGE =================
    @GetMapping("/home")
    public String home(Model model) {

        // ================= HERO (RECENT MOVIES) =================
        List<MovieDTO> heroMovies = movieRepository
                .findTop10ByOrderByMovieIdDesc()
                .stream()
                .map(this::convertToMovieDTO)
                .toList();
        model.addAttribute("heroMovies", heroMovies);

        // ================= TOP MOVIES / WEB SERIES / TV SHOWS =================
        model.addAttribute("topMovies", getTopByCategory("MOVIE"));
        model.addAttribute("topWebSeries", getTopByCategory("WEB SERIES"));
        model.addAttribute("topTVShows", getTopByCategory("TV SHOW"));

        // ================= TOP ACTORS =================
        List<PeopleDTO> topActors = peopleRepository
                .findTopPeopleByRole("ACTOR", PageRequest.of(0, 10))
                .stream()
                .map(obj -> {
                    PeopleMaster p = (PeopleMaster) obj[0];
                    Double avg = (Double) obj[1];
                    return convertToPeopleDTO(p, avg);
                })
                .toList();
        model.addAttribute("topActors", topActors);

        // ================= TOP ACTRESSES =================
        List<PeopleDTO> topActresses = peopleRepository
                .findTopPeopleByRole("ACTRESS", PageRequest.of(0, 10))
                .stream()
                .map(obj -> {
                    PeopleMaster p = (PeopleMaster) obj[0];
                    Double avg = (Double) obj[1];
                    return convertToPeopleDTO(p, avg);
                })
                .toList();
        model.addAttribute("topActresses", topActresses);

        List<String> heroSlogans = List.of(
                "Is movie pe opinion aayega.",
                "Naam bada ya kaam bhi?",
                "Screen pe magic ya sirf noise?",
                "Trailer ne hype banayi thi.",
                "Watch kiya… feel aayi?",
                "Story ya sirf star power?",
                "One-time watch ya repeat worthy?",
                "Ending yaad rahegi?",
                "Bollywood tried. Worked?",
                "Sach batao — kaisi lagi?"
        );

        model.addAttribute("heroSlogans", heroSlogans);


        return "Home";

    }

    // ======================= HELPER: GET TOP MOVIES BY CATEGORY =======================
    private List<MovieDTO> getTopByCategory(String category) {
        return movieRepository.getTop10ByCategory(category) // native query returning Object[]
                .stream()
                .map(row -> {
                    Object[] objRow = (Object[]) row; // cast Object[]
                    Long movieId = ((Number) objRow[0]).longValue();    // movie_id
                    Double avgRating = ((Number) objRow[2]).doubleValue(); // avg_rating

                    MovieMaster movie = movieRepository.findById(movieId)
                            .orElseThrow(() -> new RuntimeException("Movie not found: " + movieId));

                    MovieDTO dto = convertToMovieDTO(movie);
                    dto.setAvgRating(avgRating); // overwrite avgRating from query
                    return dto;
                })
                .toList();
    }

    // ======================= MOVIE → DTO =======================
    private MovieDTO convertToMovieDTO(MovieMaster movie) {
        MovieDTO dto = new MovieDTO(movie);

        // Cast names (limit 5)
        dto.setCastNames(movie.getMovieCasts()
                .stream()
                .map(mc -> mc.getPeople().getPeopleName())
                .limit(5)
                .toList());

        // Poster is already set in DTO constructor

        return dto;
    }

    // ======================= PEOPLE → DTO =======================
    private PeopleDTO convertToPeopleDTO(PeopleMaster p, Double avgRating) {
        PeopleDTO dto = new PeopleDTO();
        dto.setPeopleId(p.getPid());
        dto.setPeopleName(p.getPeopleName());
        dto.setAvgRating(avgRating != null ? avgRating : 0.0);

        if (p.getImage() != null) {
            dto.setImage(Base64.getEncoder().encodeToString(p.getImage()));
        }

        return dto;

    }

}