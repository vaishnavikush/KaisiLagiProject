package com.example.kaisi_lagi.UserBadgeMaster;

import com.example.kaisi_lagi.BadgeMaster.BadgeMaster;
import com.example.kaisi_lagi.UserMaster.UserMaster;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_badge",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "badge_id"}
        )
)
public class UserBadgeMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_badge_id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserMaster user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "badge_id")
    private BadgeMaster badge;

    @Column(nullable = false)
    private LocalDateTime earnedAt;

    // ===== constructors =====
    public UserBadgeMaster() {}

    public UserBadgeMaster(UserMaster user, BadgeMaster badge) {
        this.user = user;
        this.badge = badge;
        this.earnedAt = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    // ===== getters & setters =====
    public Long getId() {
        return id;
    }

    public UserMaster getUser() {
        return user;
    }

    public void setUser(UserMaster user) {
        this.user = user;
    }

    public BadgeMaster getBadge() {
        return badge;
    }

    public void setBadge(BadgeMaster badge) {
        this.badge = badge;
    }

    public LocalDateTime getEarnedAt() {
        return earnedAt;
    }

    public void setEarnedAt(LocalDateTime earnedAt) {
        this.earnedAt = earnedAt;
    }
}
