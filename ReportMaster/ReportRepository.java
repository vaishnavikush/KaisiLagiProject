package com.example.kaisi_lagi.ReportMaster;

import com.example.kaisi_lagi.AdminReportMaster.AdminReportMaster;
import com.example.kaisi_lagi.ReviewMaster.ReviewMaster;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<ReportMaster,Long> {
//    List<UserMaster>findByUserName(String username);

    //    @Query("""
//                SELECT re FROM ReportMaster re
//                JOIN FETCH re.reportMaster rq
//                JOIN FETCH rq.reporter
//                JOIN FETCH rq.owner
//                JOIN FETCH rq.reason
//                JOIN FETCH rq.report_date
//                """)
//    List<AdminReportMaster> findAllWithReportDetails();
    boolean existsByReporterAndReview(UserMaster reporter, ReviewMaster review);
    List<ReportMaster> findAllByReview(ReviewMaster review);

    //this month and last month count---
    long countByReportDateBetween(LocalDate start, LocalDate end);

}
