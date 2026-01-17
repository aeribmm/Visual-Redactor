package com.visaulredactor.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Scene {

    @Id
    private Long id;

    @OneToMany
    private List<Music> musicList;

    @OneToMany
    private List<BackgroundImage> backgroundImageList;

    @OneToMany
    private List<Character> characters;

    private String name;
    private int order;
}