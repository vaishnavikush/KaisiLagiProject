package com.example.kaisi_lagi.UserBadgeMaster;

import com.example.kaisi_lagi.UserMaster.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserBadgeController {

    @Autowired
    private UserBadgeRepository userBadgeRepository;

    @GetMapping("/my-badges")
    public String showUserBadges(
            @AuthenticationPrincipal UserMaster user,
            Model model
    ) {
        List<UserBadgeMaster> badges = userBadgeRepository.findByUser(user);

        model.addAttribute("earnedBadges", badges);

        return "badges";
    }
}
