package com.visaulredactor.repository;

import com.visaulredactor.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music,Long> {
    void deleteById(Long id);
}
