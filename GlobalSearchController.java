package com.example.kaisi_lagi;

import com.example.kaisi_lagi.MovieCast.MovieCast;
import com.example.kaisi_lagi.MovieCast.MovieCastRepository;
import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.MovieMaster.MovieRepository;
import com.example.kaisi_lagi.PeopleMaster.PeopleMaster;
import com.example.kaisi_lagi.PeopleMaster.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class GlobalSearchController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    MovieCastRepository movieCastRepository;

    // Main Search Page
    @GetMapping("/search")
    public String search(@RequestParam(name = "searchText", required = false) String searchText,
                         Model model) {
        return "GlobalSearchBar";
    }
    @GetMapping("/search/api")
    @ResponseBody
    public Map<String, List<Map<String, String>>> searchApi(
            @RequestParam("q") String searchText) {

        Map<String, List<Map<String, String>>> result = new HashMap<>();

        List<Map<String, String>> movieList = new ArrayList<>();
        List<Map<String, String>> peopleList = new ArrayList<>();

        movieRepository.findMovieByOnType(searchText).forEach(m -> {
            Map<String, String> item = new HashMap<>();
            item.put("id", m.getMovieId().toString());
            item.put("name", m.getMovieName());
            item.put("type", "Movie");
            movieList.add(item);
        });

        peopleRepository.findPeopleByRoleAndName(searchText, searchText)
                .forEach(p -> {
                    Map<String, String> item = new HashMap<>();
                    item.put("id", p.getPid().toString());
                    item.put("name", p.getPeopleName());
                    item.put("type", p.getRole());
                    peopleList.add(item);
                });

        result.put("movies", movieList);
        result.put("people", peopleList);

        return result;
    }


    // API for suggestions (used by AJAX typing)
//    @GetMapping("/search2/{searchText}")
//    @ResponseBody
//    public Map<String, List<Map<String, String>>> searchApi(
//            @PathVariable("searchText") String searchText) {
//
//        Map<String, List<Map<String, String>>> result = new HashMap<>();
//
//        List<Map<String, String>> movieList = new ArrayList<>();
//        List<Map<String, String>> peopleList = new ArrayList<>();
//
//        // Search movies
//        List<MovieMaster> movieMasterList = movieRepository.findMovieByOnType(searchText);
//        for (MovieMaster m : movieMasterList) {
//            Map<String, String> item = new HashMap<>();
//            item.put("name", m.getMovieName());
//            item.put("type", "Movie");
//            movieList.add(item);
//        }
//
//        // Search people
//        List<PeopleMaster> peopleMasterList = peopleRepository.findPeopleByRoleAndName(searchText, searchText);
//        for (PeopleMaster p : peopleMasterList) {
//            Map<String, String> item = new HashMap<>();
//            item.put("name", p.getPeopleName());
//            item.put("type", p.getRole());
//            peopleList.add(item);
//        }
//
//        result.put("movies", movieList);
//        result.put("people", peopleList);
//
//        return result;
//    }

    // Form submission when user presses enter/submit
    @GetMapping("/search2")
    public String searchFromForm(@RequestParam("keyword") String keyword,
                                 @RequestParam(value = "type", required = false) String type,
                                 Model model) {

        String q = keyword.trim();
        String t = type != null ? type.trim() : "";

        // Pehle Movie dhoondo
        List<MovieMaster> movies = movieRepository.findByMovieNameContainingIgnoreCase(q);

        if (!movies.isEmpty()) {
            MovieMaster movie = movies.get(0); // jo pehle mile
            return "redirect:/movie/detail/" + movie.getMovieId();  // ✨ Direct Movie Detail Open
        }

        // Agar Movie nahi, People search karo
        List<PeopleMaster> people = new ArrayList<>();
        if (t.isEmpty()) {
            people = peopleRepository.findMovieByOnType(q);
        } else {
            people = peopleRepository.findByRoleAndPeopleNameContainingIgnoreCase(t, q);
        }

        if (!people.isEmpty()) {
            // People ke liye list show karo (ya future me detail bhi bana denge)
            PeopleMaster person = people.get(0);
            model.addAttribute("person", person);
            return "PeopleDetailPage";
        }

        // Agar kuch nahi mile
        model.addAttribute("message", "No Result Found!");
        return "GlobalSearchBar";
    }
    @GetMapping("/movie/detail/{id:[0-9]+}")
    public String movieDetail(@PathVariable("id") Long id, Model model) {
        MovieMaster movie = movieRepository.findById(id).orElse(null);

        if (movie == null) {
            model.addAttribute("message", "Movie Not Found!");
            return "GlobalSearchBar";
        }

        model.addAttribute("movie", movie);
        return "redirect:/movies/"+id;
    }

    @GetMapping("/person/{id:[0-9]+}")
    public String getPeopleDetail(@PathVariable Long id, Model model) {

        PeopleMaster person = peopleRepository.findById(id).orElse(null);

        List<MovieCast> castList = movieCastRepository.findAllByPeople(person);

        model.addAttribute("person", person);
        model.addAttribute("castList", castList);

        return "PeopleDetailPage";
    }




}