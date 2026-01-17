package com.visaulredactor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Time;

@Entity
@Data
public class Music {

    @Id
    private Long id;
    private Long fileId;
    private Time duration;
}
