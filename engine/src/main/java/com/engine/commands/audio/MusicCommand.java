package com.engine.commands.audio;

import com.engine.commands.base.BaseCommand;
import com.engine.manager.AudioManager;

import java.util.Scanner;

public class MusicCommand extends BaseCommand {
    private final AudioManager audioManager;

    public MusicCommand(Scanner scanner, AudioManager audioManager) {
        super(scanner);
        this.audioManager = audioManager;
    }

    @Override
    public void execute() {
        System.out.println("1. Play music  2. Stop music");
        String choice = readInput("Choice: ");

        if (choice.equals("1")) {
            String musicId = readInput("Enter music ID: ");
            audioManager.playMusic(musicId);
        } else if (choice.equals("2")) {
            audioManager.stopMusic();
        }
    }

    @Override
    public String getCommandName() { return "music"; }

    @Override
    public String getDescription() { return "music - Music controls"; }
}