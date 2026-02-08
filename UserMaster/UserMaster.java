package com.example.kaisi_lagi.UserMaster;

import com.example.kaisi_lagi.ReviewMaster.ReviewMaster;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_master")
public class UserMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;
    @Column(name = "user_name", nullable = true, unique = false)
    private String username;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDate createdDate;
    private byte[] profile_pic;
    private boolean status;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "dob")

    @Nullable
    private LocalDate dob;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public byte[] getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(byte[] profile_pic) {
        this.profile_pic = profile_pic;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<ReviewMaster> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewMaster> reviews) {
        this.reviews = reviews;
    }

    @Nullable
    public LocalDate getDob() {
        return dob;
    }

    public void setDob(@Nullable LocalDate dob) {
        this.dob = dob;
    }

    public enum Role {
        ADMIN, USER
    }
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<ReviewMaster> reviews;

}
