package com.visaulredactor.repository;

import com.visaulredactor.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character,Long> {
    Character findByName(String name);
    Character findByNickname(String nickname);
    void deleteByName(String name);
    void deleteByNickname(String nickname);

}
