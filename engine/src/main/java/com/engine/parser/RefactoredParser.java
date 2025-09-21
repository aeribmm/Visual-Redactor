package com.engine.parser;

import com.engine.commands.base.Command;
import com.engine.manager.AudioManager;
import com.engine.manager.bgmanager.BackgroundManager;
import com.engine.manager.charactermanager.CharacterManager;
import com.engine.manager.ChoiceManager;
import com.engine.manager.DialogueManager;
import com.engine.manager.GameVariables;

import java.util.Scanner;

public class RefactoredParser {
    private final CommandRegistry commandRegistry;
    private final Scanner scanner;
    private boolean running = true;

    public RefactoredParser(BackgroundManager backgroundManager, CharacterManager characterManager) {
        this.scanner = new Scanner(System.in);
        this.commandRegistry = new CommandRegistry();

        // Создаем все менеджеры
        DialogueManager dialogueManager = new DialogueManager(characterManager);
        ChoiceManager choiceManager = new ChoiceManager();
        GameVariables gameVariables = new GameVariables();
        AudioManager audioManager = new AudioManager();

        // Инициализируем команды
        commandRegistry.initializeCommands(scanner, backgroundManager, characterManager,
                dialogueManager, choiceManager, gameVariables, audioManager);
    }

    public void processStory(String[] textLines) {
        System.out.println("=== Visual Novel Engine ===");
        showInitialHelp();

        int currentLineIndex = 0;

        while (running && currentLineIndex < textLines.length) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();

            if (input.equals("n")) {
                showTextLine(textLines[currentLineIndex]);
                currentLineIndex++;
            } else if (input.equals("quit")) {
                running = false;
            } else {
                processCommand(input);
                // Не двигаемся по тексту при выполнении команд
            }
        }

        if (running) {
            System.out.println("Story finished!");
        }
    }

    private void processCommand(String commandName) {
        Command command = commandRegistry.getCommand(commandName);
        if (command != null) {
            try {
                command.execute();
            } catch (Exception e) {
                System.out.println("Error executing command: " + e.getMessage());
            }
        } else {
            System.out.println("Unknown command: " + commandName + ". Type 'help' for available commands.");
        }
    }

    private void showTextLine(String text) {
        System.out.println("\n" + text);
        System.out.println("-".repeat(50));
    }

    private void showInitialHelp() {
        commandRegistry.getCommand("help").execute();
    }
}