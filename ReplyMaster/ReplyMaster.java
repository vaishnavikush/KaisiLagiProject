package com.example.kaisi_lagi.ReplyMaster;

import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.ReviewMaster.ReviewMaster;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reply_master")
public class ReplyMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;

    @Column(name = "reply_text", nullable = false)
    private String replyText;

    @Column(name = "like_count")
    private Long likeCount = 0L;

    @Column(name = "reply_date")
    private LocalDateTime replyDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserMaster user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewMaster review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private MovieMaster movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_reply_id")
    private ReplyMaster parentReply;

    @OneToMany(
            mappedBy = "parentReply",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ReplyMaster> nestedReplies = new ArrayList<>();


    public ReplyMaster() {
    }


    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public LocalDateTime getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(LocalDateTime replyDate) {
        this.replyDate = replyDate;
    }

    public UserMaster getUser() {
        return user;
    }

    public void setUser(UserMaster user) {
        this.user = user;
    }

    public ReviewMaster getReview() {
        return review;
    }

    public void setReview(ReviewMaster review) {
        this.review = review;
    }

    public MovieMaster getMovie() {
        return movie;
    }

    public void setMovie(MovieMaster movie) {
        this.movie = movie;
    }

    public ReplyMaster getParentReply() {
        return parentReply;
    }

    public void setParentReply(ReplyMaster parentReply) {
        this.parentReply = parentReply;
    }

    public List<ReplyMaster> getNestedReplies() {
        return nestedReplies;
    }

    public void setNestedReplies(List<ReplyMaster> nestedReplies) {
        this.nestedReplies = nestedReplies;
    }

}
