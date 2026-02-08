package com.example.kaisi_lagi.ReportMaster;

import com.example.kaisi_lagi.AdminReportMaster.AdminReportMaster;
import com.example.kaisi_lagi.PeopleMaster.PeopleMaster;
import com.example.kaisi_lagi.ReviewMaster.ReviewMaster;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import jakarta.persistence.*;
import org.springframework.jmx.export.annotation.ManagedAttribute;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "report_master",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"reporter_id", "review_id"})
        })
public class ReportMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long report_id;
    private String reason;
    private LocalDate reportDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserMaster user_id;
    @ManyToOne
    @JoinColumn(name = "owner")
    private UserMaster owner;

    @ManyToOne
    @JoinColumn(name = "reporter")
    private UserMaster reporter;
    @ManyToOne
    @JoinColumn(name = "review_id")
    private ReviewMaster review;
    @OneToMany(mappedBy = "reportMaster",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<AdminReportMaster>reportMasterList;


    public Long getReport_id() {
        return report_id;
    }

    public void setReport_id(Long report_id) {
        this.report_id = report_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UserMaster getUser_id() {
        return user_id;
    }

    public void setUser_id(UserMaster user_id) {
        this.user_id = user_id;
    }

    public LocalDate getReport_date() {
        return reportDate;
    }

    public void setReport_date(LocalDate report_date) {
        this.reportDate = report_date;
    }

    public UserMaster getOwner() {
        return owner;
    }

    public void setOwner(UserMaster owner) {
        this.owner = owner;
    }


    public UserMaster getReporter() {
        return reporter;
    }

    public void setReporter(UserMaster reporter) {
        this.reporter = reporter;
    }

    public ReviewMaster getReview() {
        return review;
    }

    public void setReview(ReviewMaster review) {
        this.review = review;
    }

    public ReportMaster(Long report_id, String reason, LocalDate reportDate, UserMaster user_id, UserMaster owner, UserMaster reporter, ReviewMaster review) {
        this.report_id = report_id;
        this.reason = reason;
        this.reportDate = reportDate;
        this.user_id = user_id;
        this.owner = owner;
        this.reporter = reporter;
        this.review = review;
    }

    public ReportMaster() {
    }
}
