package com.visaulredactor.repository;

import com.visaulredactor.entity.BackgroundImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackgroundRepository extends JpaRepository<BackgroundImage,Long> {
    BackgroundImage findByName(String name);
    void deleteByName(String name);
}
