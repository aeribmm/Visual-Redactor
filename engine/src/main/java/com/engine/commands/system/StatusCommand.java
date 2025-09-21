package com.engine.commands.system;

import com.engine.Character.Character;
import com.engine.Game;
import com.engine.commands.base.BaseCommand;
import com.engine.manager.charactermanager.CharacterManager;
import com.engine.manager.AudioManager;
import java.util.Scanner;

public class StatusCommand extends BaseCommand {
    private final CharacterManager characterManager;
    private final AudioManager audioManager;

    public StatusCommand(Scanner scanner, CharacterManager characterManager, AudioManager audioManager) {
        super(scanner);
        this.characterManager = characterManager;
        this.audioManager = audioManager;
    }

    @Override
    public void execute() {
        System.out.println("\n=== Game Status ===");
        System.out.println("Current background: " +
                (Game.getInstance().getCurrentBackground() != null ?
                        Game.getInstance().getCurrentBackground().getImage() : "none"));
        System.out.println("Current music: " + audioManager.getCurrentMusic());

        System.out.println("Visible characters:");
        for (Character ch : characterManager.getListOfCreatedCharacters()) {
            if (ch.isShowed()) {
                System.out.println("  " + ch.getName() + " (" + ch.getId() + ") - " +
                        ch.getPosition() + " - " + ch.getCurrentSprite());
            }
        }
    }

    @Override
    public String getCommandName() { return "status"; }

    @Override
    public String getDescription() { return "status - Show game status"; }
}