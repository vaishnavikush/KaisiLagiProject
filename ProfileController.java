package com.example.kaisi_lagi;

import com.example.kaisi_lagi.BadgeMaster.BadgeMaster;
import com.example.kaisi_lagi.BadgeMaster.BadgeRepository;
import com.example.kaisi_lagi.CategoryMaster.CategoryMaster;
import com.example.kaisi_lagi.CategoryMaster.CategoryRepository;
import com.example.kaisi_lagi.UserBadgeMaster.UserBadgeMaster;
import com.example.kaisi_lagi.UserBadgeMaster.UserBadgeRepository;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import com.example.kaisi_lagi.ReviewMaster.ReviewRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProfileController {

    @Autowired
    private UserBadgeRepository userBadgeRepository;

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {

        UserMaster user = (UserMaster) session.getAttribute("loggedUser");

       
    if (user == null) {
        return "redirect:/login";
    }

    // Add user data
    model.addAttribute("user", user);

    // Add role for frontend check
    model.addAttribute("isAdmin", user.getRole() == UserMaster.Role.ADMIN);
        model.addAttribute("activePage", "profile");
    // Profile photo
    if (user.getProfile_pic() != null) {
        model.addAttribute(
                "photo",
                Base64.getEncoder().encodeToString(user.getProfile_pic())
        );
    }

        /* =========================
           1️⃣ Earned badges (FAST lookup)
           ========================= */
        List<UserBadgeMaster> earnedBadges =
                userBadgeRepository.findByUser(user);

        Set<Long> earnedBadgeIds = earnedBadges.stream()
                .map(ub -> ub.getBadge().getBadgeId())
                .collect(Collectors.toSet());
        model.addAttribute("user",user);

        model.addAttribute("earnedBadgeIds", earnedBadgeIds);

        /* =========================
           2️⃣ Category → All badges map
           ========================= */
        List<CategoryMaster> categories = categoryRepository.findAll();

        Map<CategoryMaster, List<BadgeMaster>> categoryBadgesMap =
                new LinkedHashMap<>();

        Map<Long, Long> reviewCountMap = new HashMap<>();
        Map<Long, Integer> progressMap = new HashMap<>();

        for (CategoryMaster category : categories) {

            // All badges of this category
            List<BadgeMaster> badges =
                    badgeRepository.findByCategoryOrderByRequiredReviewsAsc(category);

            categoryBadgesMap.put(category, badges);

            // User review count
            long reviewCount =
                    reviewRepository.countByUserAndCategory(user, category);

            reviewCountMap.put(category.getId(), reviewCount);

            // Progress %
            int progress = 0;
            for (BadgeMaster b : badges) {
                if (reviewCount < b.getRequiredReviews()) {
                    progress = (int) ((reviewCount * 100) / b.getRequiredReviews());
                    break;
                }
            }

            progressMap.put(category.getId(), progress);
        }

        model.addAttribute("categoryBadgesMap", categoryBadgesMap);
        model.addAttribute("reviewCountMap", reviewCountMap);
        model.addAttribute("progressMap", progressMap);

        return "profile";
    }
    @GetMapping("/badges")
    public String badges(Model model, HttpSession session) {

        UserMaster user = (UserMaster) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        // =========================
        // Earned badges
        // =========================
        List<UserBadgeMaster> earnedBadges =
                userBadgeRepository.findByUser(user);

        Set<Long> earnedBadgeIds = earnedBadges.stream()
                .map(ub -> ub.getBadge().getBadgeId())
                .collect(Collectors.toSet());

        model.addAttribute("earnedBadgeIds", earnedBadgeIds);

        // =========================
        // Category-wise badges
        // =========================
        List<CategoryMaster> categories = categoryRepository.findAll();

        Map<CategoryMaster, List<BadgeMaster>> categoryBadgesMap =
                new LinkedHashMap<>();

        Map<Long, Long> reviewCountMap = new HashMap<>();
        Map<Long, Integer> progressMap = new HashMap<>();

        for (CategoryMaster category : categories) {

            List<BadgeMaster> badges =
                    badgeRepository.findByCategoryOrderByRequiredReviewsAsc(category);

            categoryBadgesMap.put(category, badges);

            long reviewCount =
                    reviewRepository.countByUserAndCategory(user, category);

            reviewCountMap.put(category.getId(), reviewCount);

            int progress = 0;
            for (BadgeMaster b : badges) {
                if (reviewCount < b.getRequiredReviews()) {
                    progress = (int)
                            ((reviewCount * 100) / b.getRequiredReviews());
                    break;
                }
            }
            progressMap.put(category.getId(), progress);
        }

        model.addAttribute("categoryBadgesMap", categoryBadgesMap);
        model.addAttribute("reviewCountMap", reviewCountMap);
        model.addAttribute("progressMap", progressMap);

        return "badges"; // badges.html
    }

}


