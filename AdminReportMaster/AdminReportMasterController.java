package com.example.kaisi_lagi.AdminReportMaster;

//import ch.qos.logback.core.model.Model;
import com.example.kaisi_lagi.ReportMaster.ReportMaster;
import com.example.kaisi_lagi.ReportMaster.ReportRepository;
import com.example.kaisi_lagi.ReviewMaster.ReviewMaster;
import com.example.kaisi_lagi.ReviewMaster.ReviewRepository;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import com.example.kaisi_lagi.UserMaster.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AdminReportMasterController {
    @Autowired
    AdminReportMasterRepository adminReportMasterRepository;
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminEmailService emailService;

    @Autowired
    AdminMailDelete adminMailDelete;
    @Autowired
    AdminMailDeleteReporter adminMailDeleteReporter;
    @Autowired
    AdminEmailServiceReporter adminEmailServiceReporter;
    @Autowired
    RejectEmailOwner rejectEmailOwner;
    @Autowired
    RejectEmailReporter rejectEmailReporter;


    @GetMapping("/ShowReports")
    public String ShowReports(Model model){
        Long reportMaster=reportRepository.count();
        Long reviewMaster=reviewRepository.count();
        Long userMaster=userRepository.count();
        List<ReportMaster>reportList=reportRepository.findAll();
        List<ReviewMaster>reviewList=reviewRepository.findAll();
        List<UserMaster>userList=userRepository.findAll();

        // Set report count for each review
        for (ReviewMaster review : reviewList) {
            Long count = reportRepository.countByReview(review);
            review.setReportCount(count);
        }

        model.addAttribute("reportMaster",reportMaster);
        model.addAttribute("reviewMaster",reviewMaster);
        model.addAttribute("userMaster",userMaster);
        model.addAttribute("reportList",reportList);
        model.addAttribute("reviewList",reviewList);
        model.addAttribute("userList",userList);
        model.addAttribute("activePage", "AdminReportMasterForm");




            // This Month
        LocalDate today=LocalDate.now();
        LocalDate thismonthstart=today.withDayOfMonth(1);
        LocalDate thismonthend=today.withDayOfMonth(today.lengthOfMonth());
        //Last Month
        LocalDate lastmonth=today.minusMonths(1);
        LocalDate lastmonthstart=lastmonth.withDayOfMonth(1);
        LocalDate lastmonthend=lastmonth.withDayOfMonth(lastmonth.lengthOfMonth());
        //count this month
        long thismonthreports=reportRepository.countByReportDateBetween(thismonthstart,thismonthend);
        //count last month
        long lastmonthreports=reportRepository.countByReportDateBetween(lastmonthstart,lastmonthend);

        model.addAttribute("thismonthcount",thismonthreports);
        model.addAttribute("lastmonthcount",lastmonthreports);
        return "AdminReportMasterForm";
    }

    @PostMapping("/deletereport/{report_id}")
    @Transactional
    public String delete(@PathVariable Long report_id) {

        ReportMaster reportMaster = reportRepository.findById(report_id).orElse(null);
        if (reportMaster == null) {
            return "redirect:/ShowReports";
        }

        UserMaster owner = reportMaster.getOwner();
        if (owner == null) {
            return "redirect:/login";
        }

        // Get ALL reports for the same review
        ReviewMaster review = reportMaster.getReview();
        List<ReportMaster> allReports = reportRepository.findAllByReview(review);


        // Send mail to review owner
        try {
            adminMailDelete.DeleteMailBox(owner.getEmail());
        } catch (Exception e) {
            System.out.println("Owner mail failed: " + owner.getEmail());
            e.printStackTrace();
        }

        // Send mail to ALL reporters
        for (ReportMaster report : allReports) {
            UserMaster reporter = report.getReporter();
            if (reporter != null && reporter.getEmail() != null) {
                try {
                    adminMailDeleteReporter.DeleteMailBoxReporter(reporter.getEmail());
                } catch (Exception e) {
                    System.out.println("Reporter mail failed: " + reporter.getEmail());
                    e.printStackTrace();
                }
            }
        }
        // Delete admin mappings
        adminReportMasterRepository.deleteByReportMaster(reportMaster);

        // Delete review
        if (review != null) {
            reviewRepository.deleteById(review.getReviewId());
        }

        // Delete all reports
        reportRepository.deleteAll(allReports);

        return "redirect:/ShowReports";
    }



    @GetMapping("/sendWarningMail/{report_id}")
    public String sendMail(@PathVariable("report_id") Long report_id){
        ReportMaster reportMaster = reportRepository.findById(report_id).orElse(null);
        if (reportMaster == null) {
            return "redirect:/ShowReports";
        }
        //-------------------------------------------------
        UserMaster userMaster = reportMaster.getOwner();
        if (userMaster == null) {
            return "redirect:/login";
        }
        emailService.sendWarningMail(userMaster.getEmail());
        //---------Loop----------------
        ReviewMaster reviewall=reportMaster.getReview();
        List<ReportMaster>allreports=reportRepository.findAllByReview(reviewall);

        for(ReportMaster reports:allreports) {
            UserMaster userMaster1 = reports.getReporter();
            if (userMaster1 == null) {
                return "redirect:/login";
            } else {
                adminEmailServiceReporter.sendWarningMailReporter(userMaster1.getEmail());
                reportRepository.delete(reports);
            }
        }
        return "redirect:/ShowReports";
    }


    @GetMapping("/reject/{report_id}")
    public String RejectDelete(@PathVariable("report_id") Long report_id){
        ReportMaster reports=reportRepository.findById(report_id).orElse(null);
        if(reports==null){
            return "redirect:/ShowReports";
        }
        UserMaster owner=reports.getOwner();
        if(owner==null){
            return "redirect:/login";
        }
        else {
            rejectEmailOwner.RejectOwner(owner.getEmail());
        }
        //----------------same review-----------
        ReviewMaster reviewMaster=reports.getReview();
        List<ReportMaster>allreport=reportRepository.findAllByReview(reviewMaster);

        //----------------loop-----------------
        for(ReportMaster reportMaster:allreport) {
            UserMaster reporter = reportMaster.getReporter();
            int count=(int) allreport.size();
            if(reporter==null){
                return "redirect:/login";
            }
            else {
                rejectEmailReporter.RejectReporter(reporter.getEmail());
                reportRepository.delete(reportMaster);
            }
        }
        return "redirect:/ShowReports";
    }

}

