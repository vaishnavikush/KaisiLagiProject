package com.example.kaisi_lagi.AdminReportMaster;

import com.example.kaisi_lagi.ReportMaster.ReportMaster;
import com.example.kaisi_lagi.ReviewMaster.ReviewMaster;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "adminReport")
public class AdminReportMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long admin_id;
    private LocalDateTime time;

    @ManyToOne
    private ReportMaster reportMaster;

    @ManyToOne
    private ReviewMaster reviewMaster;

    //    @ManyToOne
//    private UserMaster userMaster;
    @ManyToOne
    private UserMaster user;

    public UserMaster getUser() {
        return user;
    }

    public void setUser(UserMaster user) {
        this.user = user;
    }

    public Long getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(Long admin_id) {
        this.admin_id = admin_id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public ReportMaster getReportMaster() {
        return reportMaster;
    }

    public void setReportMaster(ReportMaster reportMaster) {
        this.reportMaster = reportMaster;
    }

    public ReviewMaster getReviewMaster() {
        return reviewMaster;
    }

    public void setReviewMaster(ReviewMaster reviewMaster) {
        this.reviewMaster = reviewMaster;
    }

    public AdminReportMaster(Long admin_id, LocalDateTime time, ReportMaster reportMaster, ReviewMaster reviewMaster) {
        this.admin_id = admin_id;
        this.time = time;
        this.reportMaster = reportMaster;
        this.reviewMaster = reviewMaster;
    }

    public AdminReportMaster() {
    }

//    public UserMaster getUserMaster() {
//        return userMaster;
//    }
//
//    public void setUserMaster(UserMaster userMaster) {
//        this.userMaster = userMaster;
//    }

}
