package com.visaulredactor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Character {

    @Id
    private Long id;
    @OneToMany
    private List<File> filesId;

    private String name;
    private String nickname;

}
