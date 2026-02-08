package com.example.kaisi_lagi.ReplyMaster;

import com.example.kaisi_lagi.MovieMaster.MovieMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<ReplyMaster, Long> {

    Long countByMovie(MovieMaster movie);
}
