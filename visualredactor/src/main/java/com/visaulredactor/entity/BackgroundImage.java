package com.visaulredactor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class BackgroundImage {

    @Id
    private Long id;
    private String name;
    private Long fileId;

}
