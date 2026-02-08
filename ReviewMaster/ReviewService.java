package com.example.kaisi_lagi.ReviewMaster;

import com.example.kaisi_lagi.UserMaster.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
    public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;


        public void saveReview(ReviewMaster review, UserMaster user) {

            review.setUser(user);
            reviewRepository.save(review);
        }
    }


