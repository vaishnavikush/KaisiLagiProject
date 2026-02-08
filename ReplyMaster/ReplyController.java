package com.example.kaisi_lagi.ReplyMaster;

import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.MovieMaster.MovieRepository;
import com.example.kaisi_lagi.ReviewMaster.ReviewMaster;
import com.example.kaisi_lagi.ReviewMaster.ReviewRepository;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ReplyController {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReplyRepository replyRepository;

    @PostMapping("/addReply")
    public String addReply(
            @RequestParam("movieId") Long movieId,
            @RequestParam("replyText") String replyText,  // ← Changed from "replayText" to "replyText"
            @RequestParam("reviewId") Long reviewId,
            HttpSession session) {

        // Check if user is logged in
        UserMaster user = (UserMaster) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<MovieMaster> movie = movieRepository.findById(movieId);
        Optional<ReviewMaster> review = reviewRepository.findById(reviewId);

        if (movie.isPresent() && review.isPresent()) {
            ReplyMaster reply = new ReplyMaster();
            reply.setReplyText(replyText);
            reply.setReview(review.get());
            reply.setMovie(movie.get());
            reply.setUser(user);
            reply.setReplyDate(LocalDateTime.now());
            replyRepository.save(reply);

            System.out.println("Reply Submitted: " + replyText);
        }

        return "redirect:/movies/" + movieId;  // ← Changed from /movies/ to /movie2/
    }

    @PostMapping("/addNestedReply")
    public String addNestedReply( @RequestParam("movieId") Long movieId,
                                  @RequestParam("replyText") String replyText,
                                  @RequestParam("reviewId")Long reviewId,
                                  @RequestParam("parentReplyId") Long parentReplyID,
                                  HttpSession session)
    {
        UserMaster user = (UserMaster) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<MovieMaster> movie = movieRepository.findById(movieId);
        Optional<ReplyMaster> parentReply = replyRepository.findById(parentReplyID);
        Optional<ReviewMaster> review= reviewRepository.findById(reviewId);
        if (movie.isPresent() && parentReply.isPresent()) {
            ReplyMaster reply = new ReplyMaster();
            reply.setReplyText(replyText);
            reply.setParentReply(parentReply.get());
            reply.setMovie(movie.get());
            reply.setUser(user);
            reply.setReview(review.get());
            reply.setReplyDate(LocalDateTime.now());
            replyRepository.save(reply);

            System.out.println("Reply Submitted: " + replyText);
        }

        return "redirect:/movies/" + movieId;
    }
}