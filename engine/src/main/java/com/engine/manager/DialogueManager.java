package com.engine.manager;

import com.engine.Character.Character;
import com.engine.manager.charactermanager.CharacterManager;

public class DialogueManager {
    private CharacterManager characterManager;

    public DialogueManager(CharacterManager characterManager) {
        this.characterManager = characterManager;
    }

    public void showDialogue(String characterId, String text) {
        Character character = characterManager.findById(characterId);
        if (character != null) {
            System.out.println("\n[" + character.getName() + "]: " + text);
        } else {
            System.out.println("\n[Unknown]: " + text);
        }
    }

    public void showNarration(String text) {
        System.out.println("\n" + text);
    }

    public void showThought(String characterId, String text) {
        Character character = characterManager.findById(characterId);
        if (character != null) {
            System.out.println("\n(" + character.getName() + " thinks): " + text);
        } else {
            System.out.println("\n(Thought): " + text);
        }
    }
}