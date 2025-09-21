package com.engine.commands.character;

import com.engine.Character.Character;
import com.engine.commands.base.BaseCommand;
import com.engine.manager.charactermanager.CharacterManager;

import java.util.Scanner;

public class ChangeSpriteCommand extends BaseCommand {
    private final CharacterManager characterManager;

    public ChangeSpriteCommand(Scanner scanner, CharacterManager characterManager) {
        super(scanner);
        this.characterManager = characterManager;
    }

    @Override
    public void execute() {
        String charId = readInput("Enter character ID: ");

        Character character = characterManager.findById(charId);
        if (character != null) {
            System.out.println("Available sprites for " + character.getName() + ":");
            character.getSprites().keySet().forEach(sprite -> System.out.println("  " + sprite));

            String spriteName = readInput("Enter sprite name: ");

            if (character.changeSprite(spriteName)) {
                System.out.println("Sprite changed to: " + spriteName);
            } else {
                System.out.println("Sprite not found!");
            }
        } else {
            System.out.println("Character not found!");
        }
    }

    @Override
    public String getCommandName() { return "sprite"; }

    @Override
    public String getDescription() { return "sprite - Change character sprite"; }
}