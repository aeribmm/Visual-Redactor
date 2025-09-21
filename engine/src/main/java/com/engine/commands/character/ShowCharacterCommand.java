package com.engine.commands.character;

import com.engine.Character.Character;
import com.engine.commands.base.BaseCommand;
import com.engine.manager.charactermanager.CharacterManager;

import java.util.Scanner;

public class ShowCharacterCommand extends BaseCommand {
    private final CharacterManager characterManager;

    public ShowCharacterCommand(Scanner scanner, CharacterManager characterManager) {
        super(scanner);
        this.characterManager = characterManager;
    }

    @Override
    public void execute() {
        System.out.println("Available characters: gg, airi");
        String charId = readInput("Enter character ID: ");

        Character character = characterManager.findById(charId);
        if (character != null) {
            characterManager.show(character);

            String position = readInput("Enter position (left/center/right)", "center");
            character.setPosition(position);
            System.out.println("Character positioned at: " + position);
        } else {
            System.out.println("Character not found!");
        }
    }

    @Override
    public String getCommandName() { return "show"; }

    @Override
    public String getDescription() { return "show - Show character"; }
}