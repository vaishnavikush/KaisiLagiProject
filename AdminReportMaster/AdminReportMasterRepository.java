package com.example.kaisi_lagi.AdminReportMaster;

import ch.qos.logback.classic.pattern.ClassOfCallerConverter;
import com.example.kaisi_lagi.ReportMaster.ReportMaster;
import com.example.kaisi_lagi.ReviewMaster.ReviewMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AdminReportMasterRepository extends JpaRepository<AdminReportMaster,Long>{

    void deleteByReportMaster(ReportMaster reportMaster);
//    List<AdminReportMaster> findByReview(ReviewMaster review);

    List<AdminReportMaster> findByReviewMaster(ReviewMaster reviewMaster);

    void deleteByReviewMaster(ReviewMaster reviewMaster);

    //    void deleteByReportMaster(ReportMaster reportMaster);
    ReviewMaster findByReportMaster(ReportMaster reportMaster);
//    List<ReportMaster> findAllByReview(ReviewMaster review);


//    //this month and last month count---
//    long reportDateBetween(LocalDate start,LocalDate end);

}
