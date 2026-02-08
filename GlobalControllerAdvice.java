package com.example.kaisi_lagi;

import com.example.kaisi_lagi.UserMaster.UserMaster;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Base64;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpSession session) {
        UserMaster user = (UserMaster) session.getAttribute("loggedUser");
        if (user != null) {
            // Profile photo
            if (user.getProfile_pic() != null) {
                model.addAttribute("photo", Base64.getEncoder().encodeToString(user.getProfile_pic()));
            }
            model.addAttribute("username", user.getUsername());
        }
    }
}