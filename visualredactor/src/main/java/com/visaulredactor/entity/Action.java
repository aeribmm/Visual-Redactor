package com.visaulredactor.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Action {

    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType type;
    private String name;
    private String description;
    private int order;
}
