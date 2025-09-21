package com.engine.commands.audio;

import com.engine.commands.base.BaseCommand;
import com.engine.manager.AudioManager;

import java.util.Scanner;

public class SoundCommand extends BaseCommand {
    private final AudioManager audioManager;

    public SoundCommand(Scanner scanner, AudioManager audioManager) {
        super(scanner);
        this.audioManager = audioManager;
    }

    @Override
    public void execute() {
        String soundId = readInput("Enter sound ID: ");
        audioManager.playSound(soundId);
    }

    @Override
    public String getCommandName() { return "sound"; }

    @Override
    public String getDescription() { return "sound - Play sound effect"; }
}