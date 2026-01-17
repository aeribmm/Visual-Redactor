package com.visaulredactor.repository;

import com.visaulredactor.entity.Scene;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SceneRepository extends JpaRepository<Scene, Long> {
    void deleteByName(String sceneName);
    Scene findByName(String name);
    List<Scene> findAllByOrder(int order);//need to replace word order
}
