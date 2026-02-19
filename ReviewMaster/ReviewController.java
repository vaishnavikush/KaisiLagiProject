package com.example.kaisi_lagi.ReviewMaster;

import com.example.kaisi_lagi.CategoryMaster.CategoryMaster;
import com.example.kaisi_lagi.CategoryMaster.CategoryRepository;
import com.example.kaisi_lagi.ImageMaster.ImageMaster;
import com.example.kaisi_lagi.ImageMaster.ImageRepository;
import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.MovieMaster.MovieRepository;
import com.example.kaisi_lagi.PeopleMaster.PeopleMaster;
import com.example.kaisi_lagi.PeopleMaster.PeopleRepository;
import com.example.kaisi_lagi.UserBadgeMaster.UserBadgeService;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import com.example.kaisi_lagi.UserMaster.UserRepository;
import com.example.kaisi_lagi.MovieCast.MovieCast;
import com.example.kaisi_lagi.MovieCast.MovieCastRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    PeopleRepository peopleRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MovieCastRepository movieCastRepository;
    @Autowired
    UserBadgeService userBadgeService;

    @GetMapping("/showpeoplerevi")
    public String showPeopleAndMovies(
            @RequestParam(defaultValue = "movies") String tab,
            @RequestParam(defaultValue = "rating_desc") String sort,
            Model model) {

        System.out.println("=== RANKING PAGE ===");
        System.out.println("Tab: " + tab + ", Sort: " + sort);

        List<PeopleMaster> peopleList = peopleRepository.findAll();
        Map<Long, Double> peopleAvgRatings = new HashMap<>();
        Map<Long, Long> peopleVotes = new HashMap<>();

        for (PeopleMaster p : peopleList) {
            Double avg10 = reviewRepository.getAvgRatingByPeople(p.getPid());
            Long votes = reviewRepository.getVoteCountByPeople(p.getPid());
            peopleAvgRatings.put(p.getPid(), avg10 == null ? 0.0 : avg10 );
            peopleVotes.put(p.getPid(), votes == null ? 0L : votes);
        }

        List<MovieMaster> movieList = movieRepository.findAll();
        Map<Long, Double> movieAvgRatings = new HashMap<>();
        Map<Long, Long> movieVotes = new HashMap<>();

        for (MovieMaster m : movieList) {
            Double avg10 = reviewRepository.getAvgRatingByMovie(m.getMovieId());
            Long votes = reviewRepository.getVoteCountByMovie(m.getMovieId());
            movieAvgRatings.put(m.getMovieId(), avg10 == null ? 0.0 : avg10 );
            movieVotes.put(m.getMovieId(), votes == null ? 0L : votes);
        }

        Map<String, List<PeopleMaster>> groupedByRole = new HashMap<>();
        for (PeopleMaster p : peopleList) {
            if (p.getRole() != null && !p.getRole().trim().isEmpty()) {
                groupedByRole.computeIfAbsent(p.getRole(), k -> new ArrayList<>()).add(p);
            }
        }

        Map<String, List<MovieMaster>> groupedByCategory = new HashMap<>();
        for (MovieMaster m : movieList) {
            if (m.getCategory() != null && m.getCategory().getName() != null) {
                String categoryName = m.getCategory().getName().trim().toLowerCase();
                groupedByCategory.computeIfAbsent(categoryName, k -> new ArrayList<>()).add(m);
            }
        }

        model.addAttribute("peopleAvgRatings", peopleAvgRatings);
        model.addAttribute("peopleVotes", peopleVotes);
        model.addAttribute("movieAvgRatings", movieAvgRatings);
        model.addAttribute("movieVotes", movieVotes);

        Map<String, Map<String, Long>> roleMedals = new HashMap<>();
        for (String role : groupedByRole.keySet()) {
            roleMedals.put(role, getTop3MedalsForPeople(groupedByRole.get(role), peopleAvgRatings, peopleVotes));
        }
        model.addAttribute("roleMedals", roleMedals);

        Map<String, Map<String, Long>> categoryMedals = new HashMap<>();
        for (String category : groupedByCategory.keySet()) {
            Map<Long, Double> avgMap = new HashMap<>();
            Map<Long, Long> voteMap = new HashMap<>();
            for (MovieMaster m : groupedByCategory.get(category)) {
                avgMap.put(m.getMovieId(), movieAvgRatings.get(m.getMovieId()));
                voteMap.put(m.getMovieId(), movieVotes.get(m.getMovieId()));
            }
            categoryMedals.put(category, getTop3Medals(avgMap, voteMap));
        }
        model.addAttribute("categoryMedals", categoryMedals);

        switch (tab) {
            case "movies":
            case "webseries":
            case "serial":
                String category = tab.equals("movies") ? "movie" : tab.equals("webseries") ? "web series" : "tv show";
                List<MovieMaster> movieTabList = groupedByCategory.getOrDefault(category, new ArrayList<>());
                sortMovies(movieTabList, movieAvgRatings, movieVotes, sort);
                model.addAttribute("movieshow", movieTabList);
                model.addAttribute("currentCategoryMedals", categoryMedals.get(category));
                break;

            case "actor":
            case "actress":
            case "director":
            case "music":
                String role = tab.equals("music") ? "Music Director" : capitalize(tab);
                List<PeopleMaster> peopleTabList = groupedByRole.getOrDefault(role, new ArrayList<>());
                sortPeople(peopleTabList, peopleAvgRatings, peopleVotes, sort);
                model.addAttribute("peopleshow", peopleTabList);
                model.addAttribute("currentRoleMedals", roleMedals.get(role));
                break;
        }

        model.addAttribute("tab", tab);
        model.addAttribute("sort", sort);
        return "ReviewShowPeopleFinal";
    }

    private Map<String, Long> getTop3Medals(Map<Long, Double> avgRatings, Map<Long, Long> votes) {
        Map<String, Long> medals = new HashMap<>();
        avgRatings.entrySet().stream()
                .filter(e -> e.getValue() != null && e.getValue() > 0)
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed()
                        .thenComparing(e -> votes.getOrDefault(e.getKey(), 0L), Comparator.reverseOrder()))
                .limit(3)
                .forEachOrdered(e -> {
                    if (!medals.containsKey("GOLD")) medals.put("GOLD", e.getKey());
                    else if (!medals.containsKey("SILVER")) medals.put("SILVER", e.getKey());
                    else medals.put("BRONZE", e.getKey());
                });
        return medals;
    }

    private Map<String, Long> getTop3MedalsForPeople(List<PeopleMaster> list, Map<Long, Double> avgRatings, Map<Long, Long> votes) {
        Map<String, Long> medals = new HashMap<>();
        list.stream()
                .filter(p -> avgRatings.getOrDefault(p.getPid(), 0.0) > 0)
                .sorted(Comparator.comparingDouble((PeopleMaster p) -> avgRatings.getOrDefault(p.getPid(), 0.0)).reversed()
                        .thenComparing(p -> votes.getOrDefault(p.getPid(), 0L), Comparator.reverseOrder()))
                .limit(3)
                .forEachOrdered(p -> {
                    if (!medals.containsKey("GOLD")) medals.put("GOLD", p.getPid());
                    else if (!medals.containsKey("SILVER")) medals.put("SILVER", p.getPid());
                    else medals.put("BRONZE", p.getPid());
                });
        return medals;
    }

    private void sortPeople(List<PeopleMaster> list, Map<Long, Double> avg, Map<Long, Long> votes, String sort) {
        switch (sort) {
            case "rating_desc" -> list.sort(Comparator.comparingDouble((PeopleMaster p) -> avg.getOrDefault(p.getPid(), 0.0)).reversed()
                    .thenComparing(p -> votes.getOrDefault(p.getPid(), 0L), Comparator.reverseOrder()));
            case "rating_asc" -> list.sort(Comparator.comparingDouble((PeopleMaster p) -> avg.getOrDefault(p.getPid(), 0.0))
                    .thenComparing(p -> votes.getOrDefault(p.getPid(), 0L), Comparator.reverseOrder()));
            case "name_asc" -> list.sort(Comparator.comparing(PeopleMaster::getPeopleName));
            case "name_desc" -> list.sort(Comparator.comparing(PeopleMaster::getPeopleName).reversed());
        }
    }

    private void sortMovies(List<MovieMaster> list, Map<Long, Double> avg, Map<Long, Long> votes, String sort) {
        switch (sort) {
            case "rating_desc" -> list.sort(Comparator.comparingDouble((MovieMaster m) -> avg.getOrDefault(m.getMovieId(), 0.0)).reversed()
                    .thenComparing(m -> votes.getOrDefault(m.getMovieId(), 0L), Comparator.reverseOrder()));
            case "rating_asc" -> list.sort(Comparator.comparingDouble((MovieMaster m) -> avg.getOrDefault(m.getMovieId(), 0.0))
                    .thenComparing(m -> votes.getOrDefault(m.getMovieId(), 0L), Comparator.reverseOrder()));
            case "name_asc" -> list.sort(Comparator.comparing(MovieMaster::getMovieName));
            case "name_desc" -> list.sort(Comparator.comparing(MovieMaster::getMovieName).reversed());
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @GetMapping("/getimagerevi/{pid}")
    public ResponseEntity<byte[]> getImage(@PathVariable("pid") Long pid) {
        PeopleMaster person = peopleRepository.findById(pid).orElse(null);
        if (person == null || person.getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(person.getImage());
    }

    @GetMapping("/getimagesok/{id}")
    public ResponseEntity<byte[]> getMovieImage(@PathVariable Long id) {
        ImageMaster img = imageRepository.findFirstByMovieMovieId(id);
        if (img == null || img.getMovieImages() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img.getMovieImages());
    }

    @GetMapping("/showrating/{pid}")
    public String showRating(@PathVariable("pid") Long pid, Model model) {
        PeopleMaster person = peopleRepository.findById(pid).orElse(null);
        if (person == null) {
            return "redirect:/error";
        }
        List<ReviewMaster> ratingList = reviewRepository.findByPeople_Pid(pid);
        model.addAttribute("person", person);
        model.addAttribute("ratings", ratingList);
        return "ShowPeopleRating";
    }

    @PostMapping("/setRating")
    public String setReview(@RequestParam("rating") int rating, @RequestParam("movieId") Long movieId, HttpSession session) {
        UserMaster user = (UserMaster) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        Optional<MovieMaster> movieOpt = movieRepository.findById(movieId);
        if (movieOpt.isEmpty()) {
            return "redirect:/error";
        }
        MovieMaster movie = movieOpt.get();
        if (movie.getCategory() == null) {
            return "redirect:/error";
        }
        List<ReviewMaster> reviews = reviewRepository.findByMovieAndUser(movie, user);
        boolean reviewUpdated = false;
        if (reviews != null && !reviews.isEmpty()) {
            for (ReviewMaster r : reviews) {
                if (r.getPeople() == null) {
                    r.setRating(rating);
                    r.setCategory(movie.getCategory());
                    r.setReviewDate(LocalDateTime.now());
                    reviewRepository.save(r);
                    reviewUpdated = true;
                    break;
                }
            }
        }
        if (!reviewUpdated) {
            ReviewMaster newReview = new ReviewMaster();
            newReview.setCategory(movie.getCategory());
            newReview.setRating(rating);
            newReview.setUser(user);
            newReview.setMovie(movie);
            newReview.setReviewDate(LocalDateTime.now());
            newReview.setLikeCount(0L);
            reviewRepository.save(newReview);
        }
        try {
            userBadgeService.checkAndAssignBadges(user, movie.getCategory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.setAttribute("MovieRateSuccess", true);
        return "redirect:/movies/" + movieId;
    }

    @PostMapping("/setRatting")
    public String setRating2(HttpSession session, @RequestParam("rating") int rating, @RequestParam("movieId") Long movieId) {
        return setReview(rating, movieId, session);
    }

    @PostMapping("addComment")
    public String addComment(@RequestParam("comment") String comment, @RequestParam("movieId") Long movieId, HttpSession session) {
        UserMaster user = (UserMaster) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        Optional<MovieMaster> movieOpt = movieRepository.findById(movieId);
        if (movieOpt.isEmpty()) {
            return "redirect:/error";
        }
        MovieMaster movie = movieOpt.get();
        List<ReviewMaster> reviews = reviewRepository.findByMovieAndUser(movie, user);
        boolean commentAdded = false;
        if (reviews != null && !reviews.isEmpty()) {
            for (ReviewMaster r : reviews) {
                if (r.getPeople() == null && r.getComment() == null) {
                    r.setComment(comment);
                    r.setReviewDate(LocalDateTime.now());
                    reviewRepository.save(r);
                    commentAdded = true;
                    break;
                }
            }
        }
        if (!commentAdded) {
            ReviewMaster freshComment = new ReviewMaster();
            freshComment.setMovie(movie);
            freshComment.setUser(user);
            freshComment.setReviewDate(LocalDateTime.now());
            freshComment.setComment(comment);
            reviewRepository.save(freshComment);
        }
        return "redirect:/movies/" + movieId;
    }

    @GetMapping("/CastRating/{MovieId}")
    public String castRating(@PathVariable Long MovieId, Model model, HttpServletRequest request) {
        String previousURL = request.getHeader("Referer");
        model.addAttribute("url", previousURL);
        Optional<MovieMaster> movieOpt = movieRepository.findById(MovieId);
        if (movieOpt.isEmpty()) {
            return "redirect:/error";
        }
        MovieMaster movie = movieOpt.get();
        List<MovieCast> cast = movieCastRepository.findAllByMovie(movie);
        List<PeopleMaster> otherCast = new ArrayList<>();
        List<PeopleMaster> actors = new ArrayList<>();
        List<PeopleMaster> actresses = new ArrayList<>();
        List<PeopleMaster> singers = new ArrayList<>();
        List<PeopleMaster> directors = new ArrayList<>();
        List<PeopleMaster> writers = new ArrayList<>();
        List<PeopleMaster> producers = new ArrayList<>();
        for (MovieCast c : cast) {
            PeopleMaster person = c.getPeople();
            String role = person.getRole();
            if (role == null) {
                otherCast.add(person);
                continue;
            }
            switch (role.toLowerCase()) {
                case "actor" -> actors.add(person);
                case "actress" -> actresses.add(person);
                case "singer" -> singers.add(person);
                case "director" -> directors.add(person);
                case "writer" -> writers.add(person);
                case "producer" -> producers.add(person);
                default -> otherCast.add(person);
            }
        }
        model.addAttribute("ppl", otherCast);
        model.addAttribute("singer", singers);
        model.addAttribute("actor", actors);
        model.addAttribute("director", directors);
        model.addAttribute("actress", actresses);
        model.addAttribute("writer", writers);
        model.addAttribute("producer", producers);
        model.addAttribute("movie", movie);
        return "CastRatingPage";
    }

    @PostMapping("/CastRateSubmit")
    public String castRateSubmit(@RequestParam("peopleId[]") List<Long> castIds, @RequestParam("movieId") Long movieId,
                                 HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        UserMaster user = (UserMaster) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        Optional<MovieMaster> movieOpt = movieRepository.findById(movieId);
        if (movieOpt.isEmpty()) {
            return "redirect:/error";
        }
        MovieMaster movie = movieOpt.get();
        for (Long pid : castIds) {
            String fieldName = "rating_" + pid;
            String ratingStr = request.getParameter(fieldName);
            if (ratingStr == null || ratingStr.isEmpty()) {
                continue;
            }
            int rating = Integer.parseInt(ratingStr);
            if (rating == 0) {
                continue;
            }
            Optional<PeopleMaster> personOpt = peopleRepository.findById(pid);
            if (personOpt.isEmpty()) {
                continue;
            }
            PeopleMaster person = personOpt.get();
            Optional<ReviewMaster> existingReview = reviewRepository.findUserPeopleReview(movieId, user.getId(), pid);
            if (existingReview.isPresent()) {
                ReviewMaster review = existingReview.get();
                review.setRating(rating);
                review.setReviewDate(LocalDateTime.now());
                reviewRepository.save(review);
            } else {
                ReviewMaster review = new ReviewMaster();
                review.setRating(rating);
                review.setCategory(movie.getCategory());
                review.setReviewDate(LocalDateTime.now());
                review.setUser(user);
                review.setMovie(movie);
                review.setPeople(person);
                reviewRepository.save(review);
            }
        }
        session.setAttribute("MovieRateSuccess", true);
        return "redirect:/movies/" + movieId;
    }
}
