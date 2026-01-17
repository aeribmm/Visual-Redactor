package com.visaulredactor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class File {
    @Id
    private Long id;
    private String name;
    private String content;
    private String type;
    private double size;
}
