package com.example.kaisi_lagi.MovieMaster;
import com.example.kaisi_lagi.CategoryMaster.CategoryMaster;
import com.example.kaisi_lagi.CategoryMaster.CategoryRepository;
import com.example.kaisi_lagi.ImageMaster.ImageMaster;
import com.example.kaisi_lagi.ImageMaster.ImageRepository;
import com.example.kaisi_lagi.MovieCast.MovieCast;
import com.example.kaisi_lagi.MovieCast.MovieCastRepository;
import com.example.kaisi_lagi.MovieCast.MovieCastService;
import com.example.kaisi_lagi.PeopleMaster.PeopleMaster;
import com.example.kaisi_lagi.PeopleMaster.PeopleRepository;
import com.example.kaisi_lagi.ReviewMaster.ReviewMaster;
import com.example.kaisi_lagi.ReviewMaster.ReviewRepository;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import com.example.kaisi_lagi.UserMaster.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import javax.swing.text.html.StyleSheet;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    PeopleRepository peopleRepository;
    @Autowired
    MovieCastRepository movieCastRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRepository reviewRepository;

    @GetMapping("/add")
    public String addMovie(Model model) {
        model.addAttribute("movie", new MovieMaster());
        model.addAttribute("categories", categoryRepository.findAll());
        return "AddMovie";
    }

    @Transactional
    @PostMapping("/saveMovie")
    public String saveMovie(
            @ModelAttribute("movie") MovieMaster movie,
            BindingResult result,
            @RequestParam Long categoryId,
            @RequestParam(required = false, name = "veers") MultipartFile[] veers,
            Model model,
            @RequestParam(required = false, name = "url") String url
    ) throws IOException {

        if (result.hasErrors()) {
            return "AddMovie";
        }

        // Format movie name
        String finalName = Arrays.stream(movie.getMovieName().trim().split(" "))
                .filter(w -> !w.isEmpty())
                .map(w -> Character.toUpperCase(w.charAt(0)) + w.substring(1))
                .collect(Collectors.joining(" "));
        movie.setMovieName(finalName);

        // Set category
        CategoryMaster category = categoryRepository.findById(categoryId).orElse(null);
        movie.setCategory(category);

        movieRepository.save(movie);

        List<ImageMaster> masterList = new ArrayList<>();

        // Save movie poster images
        if (veers != null && veers.length > 0) {
            System.out.println("=== PROCESSING IMAGES ===");
            System.out.println("Total files received: " + veers.length);

            for (int i = 0; i < veers.length; i++) {
                MultipartFile file = veers[i];
                System.out.println("File " + i + ": " +
                        (file != null ? "Name=" + file.getOriginalFilename() +
                                ", Size=" + file.getSize() +
                                ", Empty=" + file.isEmpty() : "NULL"));

                // Only process non-empty files
                if (file != null && !file.isEmpty() && file.getSize() > 0) {
                    ImageMaster img = new ImageMaster();
                    img.setMovieImages(file.getBytes());
                    img.setMovie(movie);
                    masterList.add(img);
                    System.out.println("✓ Added to masterList");
                } else {
                    System.out.println("✗ Skipped (null or empty)");
                }
            }
            System.out.println("Total images to save: " + masterList.size());
        }

        // Save trailer URL as a separate image entry (if provided)
        if (url != null && !url.trim().isEmpty()) {
            System.out.println("Adding trailer URL: " + url);
            ImageMaster trailerImg = new ImageMaster();
            trailerImg.setUrl(url.trim());
            trailerImg.setMovie(movie);
            masterList.add(trailerImg);
        }

        // Save all images in one batch
        if (!masterList.isEmpty()) {
            System.out.println("Saving " + masterList.size() + " images to database");
            imageRepository.saveAll(masterList);
        }

        // Trigger Lottie success animation
        model.addAttribute("success", true);
        model.addAttribute("movie", movie);

        return "AddMovie";
    }

    @GetMapping("/addCast/{movieId}")
    public String addCastPage(@PathVariable Long movieId, Model model) {
        MovieMaster movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) {
            model.addAttribute("error", "Movie not found!");
            return "error";
        }

        List<PeopleMaster> allPeople = peopleRepository.findAll()
                .stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(PeopleMaster::getPeopleName, p -> p, (p1, p2) -> p1),
                        m -> new ArrayList<>(m.values())
                ));

        List<String> roles = List.of("actors","actresses","directors","singers","lyricists","music_director");

        model.addAttribute("movie", movie);
        model.addAttribute("allPeople", allPeople);
        model.addAttribute("roles", roles);


        return "AddCast";
    }


    @PostMapping("/saveCast")
    public String saveCast(
            @RequestParam Long movieId,
            @RequestParam(required=false) List<Long> actors,
            @RequestParam(required=false) List<Long> actresses,
            @RequestParam(required=false) List<Long> directors,
            @RequestParam(required=false) List<Long> singers,
            @RequestParam(required=false) List<Long> lyricists,
            @RequestParam(required=false) List<Long> music_Director,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        MovieMaster movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) return "error";

        saveMovieCastById(movie, actors, "actor");
        saveMovieCastById(movie, actresses, "actress");
        saveMovieCastById(movie, directors, "director");
        saveMovieCastById(movie, singers, "singer");
        saveMovieCastById(movie, lyricists, "lyricist");
        saveMovieCastById(movie,music_Director,"music_director");

        model.addAttribute("success",true);
        return "ShowMovies";
    }


    private void saveMovieCastById(MovieMaster movie, List<Long> peopleIds, String role) {
        if (peopleIds != null) {
            for (Long id : peopleIds) {
                PeopleMaster person = peopleRepository.findById(id).orElse(null);
                if (person != null) {
                    boolean alreadyAssigned = movieCastRepository.existsByMovieAndPeople(movie, person);
                    if (!alreadyAssigned) {
                        MovieCast cast = new MovieCast();
                        cast.setMovie(movie);
                        cast.setPeople(person);
                        cast.setRole(role);
                        movieCastRepository.save(cast);
                    }
                }
            }
        }
    }


    @GetMapping("/movies")
    public String moviesPage(@RequestParam(required = false) String q,
                             @RequestParam(required = false) String category,
                             Model model) {

        System.out.println("=== [MOVIES PAGE LOAD] ===");
        System.out.println("Search Query: " + q);
        System.out.println("Category: " + category);
        System.out.println("Loading first 9 movies...");

        PageRequest pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "movieId"));


        String query = (q == null || q.isBlank()) ? null : "%" + q.trim().toLowerCase() + "%";
        String cat = (category == null || category.isBlank()) ? null : category.trim().toLowerCase();

        Page<MovieMaster> movies = movieRepository.searchAllFields(query, cat, pageable);
        System.out.println("Movies found: " + movies.getTotalElements());

        List<MovieDTO> movieDTOs = movies.getContent()
                .stream()
                .map(movie -> {
                    System.out.println("Converting Movie ID: " + movie.getMovieId());
                    return convertToDTO(movie);
                })
                .toList();

        model.addAttribute("movies", movieDTOs);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("category", category == null ? "" : category);

        return "ShowMovies";
    }


    @GetMapping("/movies/load")
    @ResponseBody
    public Page<MovieDTO> loadMovies(
            @RequestParam int page,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category) {

        System.out.println("\n=== [INFINITE SCROLL] ===");
        System.out.println("Requested Page: " + page);
        System.out.println("Search Query: " + q);
        System.out.println("Category: " + category);

        PageRequest pageable = PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "movieId"));

        String query = (q == null || q.isBlank()) ? null : "%" + q.trim().toLowerCase() + "%";
        String cat = (category == null || category.isBlank()) ? null : category.trim().toLowerCase();


        Page<MovieMaster> movies = movieRepository.searchAllFields(query, cat, pageable);

        System.out.println("Movies Returned: " + movies.getNumberOfElements());
        movies.getContent().forEach(m ->
                System.out.println("Movie ID Loaded: " + m.getMovieId() + " | Name: " + m.getMovieName())
        );

        return movies.map(this::convertToDTO);
    }

    private MovieDTO convertToDTO(MovieMaster movie) {

        System.out.println("[DTO] Start mapping for Movie: " + movie.getMovieId());

        MovieDTO dto = new MovieDTO(movie);

        List<MovieCast> casts =
                movieCastRepository.findCastsByMovie(movie.getMovieId());

        // ✅ ACTORS + ACTRESSES (MIX)
        dto.setCastNames(
                casts.stream()
                        .filter(mc ->
                                "actor".equalsIgnoreCase(mc.getRole()) ||
                                        "actress".equalsIgnoreCase(mc.getRole())
                        )
                        .map(mc -> mc.getPeople().getPeopleName())
                        .distinct()
                        .limit(5)   // premium look
                        .toList()
        );

        System.out.println("[DTO] Cast: " + dto.getCastNames());

        return dto;
    }



    @GetMapping("/autocomplete")
    @ResponseBody
    public List<String> autocomplete(@RequestParam String term) {
        return movieRepository.searchAutocomplete(term);
    }


    @GetMapping("/edit/{id:[0-9]+}")
    public String editMovie(@PathVariable Long id, Model model) {
        MovieMaster movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie Not Found"));
        model.addAttribute("movie", movie);

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("allGenres", List.of("Action", "Thriller", "Horror", "Romance", "Crime", "Comedy", "Drama", "Other"));
        model.addAttribute("allLanguages", List.of("English", "Hindi", "Tamil", "Telugu", "Kannada", "Other"));


        List<PeopleMaster> allPeople = peopleRepository.findAll()
                .stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(PeopleMaster::getPeopleName, p -> p, (existing, replacement) -> existing),
                        m -> new ArrayList<>(m.values())
                ));

        model.addAttribute("actors", allPeople);
        model.addAttribute("actresses", allPeople);
        model.addAttribute("directors", allPeople);
        model.addAttribute("lyricists", allPeople);
        model.addAttribute("singers", allPeople);
        model.addAttribute("musicDirectors",allPeople);

        return "EditMovie";
    }
    @Autowired
    private MovieCastService movieCastService;

    @PostMapping("/updateMovie")
    @Transactional
    public String updateMovie(
            @RequestParam Long movieId,
            @RequestParam(required = false) List<Long> actorIds,
            @RequestParam(required = false) List<Long> actressIds,
            @RequestParam(required = false) List<Long> directorIds,
            @RequestParam(required = false) List<Long> lyricistIds,
            @RequestParam(required = false) List<Long> musicDirectorsIds,
            @RequestParam(value = "photos", required = false) MultipartFile[] photos,
            RedirectAttributes redirectAttributes) throws Exception
    {
        MovieMaster movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movieCastService.updateCastByRole(movie, "Actor", actorIds);
        movieCastService.updateCastByRole(movie, "Actress", actressIds);
        movieCastService.updateCastByRole(movie, "Director", directorIds);
        movieCastService.updateCastByRole(movie, "Lyricist", lyricistIds);
        movieCastService.updateCastByRole(movie, "Music Director", musicDirectorsIds);

        /* ===== IMAGE UPDATE (OPTIONAL) ===== */
        boolean newImage = false;
        for (MultipartFile file : photos) {
            if (!file.isEmpty()) {
                newImage = true;
                break;
            }
        }

        if (newImage) {
            imageRepository.deleteByMovieMovieId(movie.getMovieId());
            movie.getImages().clear();
            for (MultipartFile file : photos) {
                if (!file.isEmpty()) {
                    ImageMaster image = new ImageMaster();
                    image.setMovieImages(file.getBytes());
                    image.setMovie(movie);
                    movie.getImages().add(image);
                }
            }
        }

        movieRepository.save(movie);

        // Use redirect attributes to pass success parameter
        return "redirect:/editMovie?id=" + movieId + "&success=true";
    }


    @GetMapping("/checkMovie")
    @ResponseBody
    public Map<String, Object> checkMovie(@RequestParam String movieName) {
        Map<String, Object> response = new HashMap<>();

        List<MovieMaster> movies = movieRepository.findByMovieNameIgnoreCase(movieName);

        if (!movies.isEmpty()) {
            response.put("exists", true);
            response.put("releaseDate", movies.get(0).getReleaseDate().toString());
        } else {
            response.put("exists", false);
        }

        return response;
    }
    @GetMapping("/moviedelete")
    public String listMovies(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sort", defaultValue = "movieName") String sort,
            @RequestParam(value = "dir", defaultValue = "asc") String dir,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Sort sortBy;

        if ("category".equals(sort)) {
            sortBy = Sort.by(dir.equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC, "category.name");
        } else {
            sortBy = Sort.by(dir.equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC, "movieName");
        }

        Pageable pageable = PageRequest.of(page, 10, sortBy);

        String keyword = (q == null || q.isBlank())
                ? null
                : "%" + q.trim() + "%";

        String categoryParam = (category == null || category.isBlank())
                ? null
                : category.trim();

        Page<MovieMaster> moviesPage =
                movieRepository.searchAllFields(keyword, categoryParam, pageable);

        model.addAttribute("movies", moviesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviesPage.getTotalPages());
        model.addAttribute("q", q);
        model.addAttribute("category", category);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);

        return "Allmovies";
    }

    @PostMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {

        MovieMaster movieMaster = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie Not Found "));
        movieRepository.delete(movieMaster);
        return "redirect:/Allmovies";
    }


}