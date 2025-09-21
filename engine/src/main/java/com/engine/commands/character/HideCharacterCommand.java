package com.engine.commands.character;

import com.engine.Character.Character;
import com.engine.commands.base.BaseCommand;
import com.engine.manager.charactermanager.CharacterManager;

import java.util.Scanner;

public class HideCharacterCommand extends BaseCommand {
    private final CharacterManager characterManager;

    public HideCharacterCommand(Scanner scanner, CharacterManager characterManager) {
        super(scanner);
        this.characterManager = characterManager;
    }

    @Override
    public void execute() {
        System.out.println("Available characters: gg, airi");
        String charId = readInput("Enter character ID: ");

        Character character = characterManager.findById(charId);
        if (character != null) {
            characterManager.hide(character);
        } else {
            System.out.println("Character not found!");
        }
    }

    @Override
    public String getCommandName() { return "hide"; }

    @Override
    public String getDescription() { return "hide - Hide character"; }
}