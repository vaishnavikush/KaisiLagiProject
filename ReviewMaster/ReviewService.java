package com.example.kaisi_lagi.ReviewMaster;

import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
    public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;


        public void saveReview(ReviewMaster review, UserMaster user, Model model, MovieMaster movie) {

            review.setUser(user);
            reviewRepository.save(review);

            if (user != null) {
                List<ReviewMaster> reviews = reviewRepository.findUserMovieReviewByObjects(user, movie);
                if (!reviews.isEmpty()) {
                    ReviewMaster r = reviews.get(0); // take the latest
                    model.addAttribute("rateExists", r.getRating());
                    model.addAttribute("commentExists", r.getComment());
                }
            }
        }
    }


