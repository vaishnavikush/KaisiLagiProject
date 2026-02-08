package com.example.kaisi_lagi.ReportMaster;

//import ch.qos.logback.core.model.Model;
import com.example.kaisi_lagi.ReviewMaster.ReviewMaster;
import com.example.kaisi_lagi.ReviewMaster.ReviewRepository;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import com.example.kaisi_lagi.UserMaster.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

//import static jdk.nio.zipfs.ZipFileAttributeView.AttrID.owner;

@Controller
public class ReportController {
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRepository reviewRepository;

    @GetMapping("/setreport/{reviewId}")
    public String ReportSet(@PathVariable("reviewId") Long reviewId, HttpSession session) {
        ReviewMaster reviewMaster = reviewRepository.findById(reviewId).orElse(null);

        session.setAttribute("reviewMaster", reviewMaster);

        return "ReportMasterPackage/SetReportForm";
    }

    @PostMapping("/reportset/{reviewId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> setReport(
            @PathVariable Long reviewId,
            @RequestBody Map<String, String> body,
            HttpSession session) {

        String reason = body.get("reason");

        UserMaster currentUser = (UserMaster) session.getAttribute("loggedUser");
        ReviewMaster reviewMaster = reviewRepository.findById(reviewId).orElse(null);

        if (currentUser == null || reviewMaster == null || reason == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid request"));
        }

        UserMaster reporter = userRepository.findById(currentUser.getId()).orElse(null);
        if (reporter == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid user"));
        }

        if (reportRepository.existsByReporterAndReview(reporter, reviewMaster)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Already reported"));
        }

        ReportMaster reportMaster = new ReportMaster();
        reportMaster.setReason(reason);
        reportMaster.setReporter(reporter);
        reportMaster.setReview(reviewMaster);
        reportMaster.setOwner(reviewMaster.getUser());
        reportMaster.setReport_date(LocalDate.now());

        reportRepository.save(reportMaster);

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}