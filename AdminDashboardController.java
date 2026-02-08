package com.example.kaisi_lagi;

import com.example.kaisi_lagi.CategoryMaster.CategoryRepository;
import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.MovieMaster.MovieRepository;
import com.example.kaisi_lagi.ReviewMaster.ReviewRepository;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import com.example.kaisi_lagi.UserMaster.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class AdminDashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // show user list
    @GetMapping("/users")
    public String users(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        model.addAttribute("activePage", "makeAdmin");
        UserMaster admin = (UserMaster) session.getAttribute("loggedUser");

        if (admin == null || admin.getRole() != UserMaster.Role.ADMIN) {
            return "redirect:/login";
        }

        model.addAttribute("users", userRepository.findAll());
        return "newAdmin";
    }

    @PostMapping("/make-admin/{id}")
    public String makeAdmin(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {

        UserMaster admin = (UserMaster) session.getAttribute("loggedUser");

        if (admin == null || admin.getRole() != UserMaster.Role.ADMIN) {
            return "redirect:/login";
        }

        UserMaster user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(UserMaster.Role.ADMIN);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("toastMsg", "User promoted to Admin successfully");
        redirectAttributes.addFlashAttribute("toastType", "success");

        return "redirect:/users";
    }

    @GetMapping("/block-users")
    public String blockUsers(Model model, HttpSession session) {

        UserMaster admin = (UserMaster) session.getAttribute("loggedUser");
        model.addAttribute("activePage", "blockUser");
        if (admin == null || admin.getRole() != UserMaster.Role.ADMIN) {
            return "redirect:/login";
        }

        model.addAttribute("users", userRepository.findAll());
        return "blockUser";
    }

    @PostMapping("/block-user/{id}")
    public String blockUser(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {

        UserMaster admin = (UserMaster) session.getAttribute("loggedUser");

        if (admin == null || admin.getRole() != UserMaster.Role.ADMIN) {
            return "redirect:/login";
        }

        UserMaster user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // prevent admin blocking themselves
        if (user.getId().equals(admin.getUsername())) {
            return "redirect:/block-users";
        }

        user.setStatus(false);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("toastMsg", "User blocked successfully");
        redirectAttributes.addFlashAttribute("toastType", "danger");

        return "redirect:/block-users";
    }

    @PostMapping("/unblock-user/{id}")
    public String unblockUser(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {

        UserMaster admin = (UserMaster) session.getAttribute("loggedUser");

        if (admin == null || admin.getRole() != UserMaster.Role.ADMIN) {
            return "redirect:/login";
        }

        UserMaster user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(true);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("toastMsg", "User unblocked successfully");
        redirectAttributes.addFlashAttribute("toastType", "success");

        return "redirect:/block-users";
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Model model,
            HttpSession session,
            @RequestParam(value = "movieName", required = false) String movieName) {

        UserMaster user = (UserMaster) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        // Profile photo and username are now added globally via GlobalControllerAdvice

        // Optional: Fetch a movie by name
        MovieMaster movie = null;
        if (movieName != null && !movieName.isBlank()) {
            movie = movieRepository.findByMovieNameContainingIgnoreCase(movieName.trim())
                    .stream()
                    .findFirst()
                    .orElse(null);
        }
        model.addAttribute("movie", movie);

        try {
            // Users stats
            model.addAttribute("totalUsers", userRepository.count());
            model.addAttribute("activeUsers", userRepository.countByStatus(true));
            model.addAttribute("inactiveUsers", userRepository.countByStatus(false));

            // Movies/Reviews stats
            model.addAttribute("totalMovies", movieRepository.count());
            model.addAttribute("totalReviews", reviewRepository.count());

            // Top rated items
            List<Object[]> topMovies = reviewRepository.getTopMovie();
            List<Object[]> topWeb = reviewRepository.getTopWebseries();
            List<Object[]> topTv = reviewRepository.getTopTvShow();

            model.addAttribute("topMovie", convertTopItem(topMovies.isEmpty() ? null : topMovies.get(0), "Movie"));
            model.addAttribute("topWeb", convertTopItem(topWeb.isEmpty() ? null : topWeb.get(0), "Web Series"));
            model.addAttribute("topSerial", convertTopItem(topTv.isEmpty() ? null : topTv.get(0), "TV Show"));

            // Category counts
            long movieCount = movieRepository.countByCategory_NameIgnoreCase("MOVIE");
            long webseriesCount = movieRepository.countByCategory_NameIgnoreCase("WEB SERIES");
            long tvShowCount = movieRepository.countByCategory_NameIgnoreCase("TV SHOW");

            model.addAttribute("movieCount", movieCount);
            model.addAttribute("webseriesCount", webseriesCount);
            model.addAttribute("serialCount", tvShowCount);
            model.addAttribute("totalEntries", movieCount + webseriesCount + tvShowCount);

            // Charts data
            populateChartData(model, reviewRepository.getDailyReviews(), "dailyLabels", "dailyCounts");
            populateChartData(model, userRepository.getMonthlyNewUsers(), "monthlyLabels", "monthlyCounts");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "adminDashBoard";
    }

    // Helper: Convert Object[] row to TopItem
    private TopItem convertTopItem(Object[] row, String type) {
        if (row != null && row.length >= 2) {
            String name = row[0] != null ? row[0].toString() : "N/A";
            String rating;
            try {
                rating = row[1] != null ? String.format("%.1f", Double.parseDouble(row[1].toString())) : "0.0";
            } catch (NumberFormatException e) {
                rating = "0.0";
            }
            return new TopItem(name, rating, type);
        }
        return new TopItem("N/A", "0.0", type);
    }

    // Helper: Populate chart data
    private void populateChartData(Model model, List<Object[]> rows, String labelAttr, String countAttr) {
        List<String> labels = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        if (rows != null) {
            for (Object[] row : rows) {
                labels.add(row[0] != null ? row[0].toString() : "N/A");
                try {
                    counts.add(row[1] != null ? Integer.parseInt(row[1].toString()) : 0);
                } catch (NumberFormatException e) {
                    counts.add(0);
                }
            }
        }

        model.addAttribute(labelAttr, labels);
        model.addAttribute(countAttr, counts);
    }

    // TopItem class for Thymeleaf
    public static class TopItem {
        private final String name;
        private final String rating;
        private final String type;

        public TopItem(String name, String rating, String type) {
            this.name = name;
            this.rating = rating;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getRating() {
            return rating;
        }

        public String getType() {
            return type;
        }
    }
}