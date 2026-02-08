package com.example.kaisi_lagi.ReviewMaster;

import com.example.kaisi_lagi.CategoryMaster.CategoryMaster;
import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import com.example.kaisi_lagi.PeopleMaster.PeopleMaster;
import com.example.kaisi_lagi.ReplyMaster.ReplyMaster;
import com.example.kaisi_lagi.ReportMaster.ReportMaster;
import com.example.kaisi_lagi.SubCategory;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "review_master")
public class ReviewMaster {
    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public MovieMaster getMovie() {
        return movie;
    }

    public PeopleMaster getPeople() {
        return people;
    }

    public void setPeople(PeopleMaster people) {
        this.people = people;
    }

    public void setMovie(MovieMaster movie) {
        this.movie = movie;
    }

    public UserMaster getUser() {
        return user;
    }

    public void setUser(UserMaster user) {
        this.user = user;
    }


    public CategoryMaster getCategory() {
        return category;
    }

    public void setCategory(CategoryMaster category) {
        this.category = category;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<ReplyMaster> replies;

    // ADD THIS: Transient field to get reply count
    @Transient
    public int getReplyCount() {
        return replies != null ? replies.size() : 0;
    }

    // ADD THIS: Getter for replies
    public List<ReplyMaster> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyMaster> replies) {
        this.replies = replies;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;
    private int rating;
//    @Column(name = "comment", nullable = true)
//    private String comment;
@Column(name = "comment")
private String comment = ""; // Add default value
    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @Column(name="like_count")
    private Long likeCount=0L;

    @ManyToOne()
    @JoinColumn(name = "movie_id")
    private MovieMaster movie;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserMaster user;

    @ManyToOne()
    @JoinColumn(name="people_id")
    private PeopleMaster people;

    @ManyToOne()
    @JoinColumn(name= "cate_id")
    private CategoryMaster category;
    @OneToMany(
            mappedBy = "review",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ReportMaster> reports;

}