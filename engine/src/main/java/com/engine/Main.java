package com.engine;

import com.engine.manager.bgmanager.BackgroundManager;
import com.engine.Character.Character;
import com.engine.manager.charactermanager.CharacterManager;
import com.engine.parser.RefactoredParser;


public class Main {
    public static void main(String[] args) {
        Game game = Game.getInstance();

        String[] story = {
                "Весна — время перемен.",
                "Когда холод начинает отступать, а на смену ему приходит тепло, сакура расцветает.",
                "В то же время, она знаменует начало нового учебного года.",
                "И никто из них не может знать наверняка, что она им принесет."
        };

        BackgroundManager backgroundManager = new BackgroundManager();
        CharacterManager characterManager = new CharacterManager();



        RefactoredParser parser = new RefactoredParser(backgroundManager, characterManager);
        parser.processStory(story);
    }
}