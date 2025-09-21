package com.engine.parser;

import com.engine.commands.audio.MusicCommand;
import com.engine.commands.audio.SoundCommand;
import com.engine.commands.background.ChangeBackgroundCommand;
import com.engine.commands.base.Command;
import com.engine.commands.character.ChangeSpriteCommand;
import com.engine.commands.character.HideCharacterCommand;
import com.engine.commands.character.ShowCharacterCommand;
import com.engine.commands.dialogue.SayCommand;
import com.engine.commands.dialogue.ThinkCommand;
import com.engine.commands.system.*;
import com.engine.manager.AudioManager;
import com.engine.manager.bgmanager.BackgroundManager;
import com.engine.manager.charactermanager.CharacterManager;
import com.engine.manager.ChoiceManager;
import com.engine.manager.DialogueManager;
import com.engine.manager.GameVariables;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandRegistry {
    private final Map<String, Command> commands = new HashMap<>();

    public void registerCommand(Command command) {
        commands.put(command.getCommandName(), command);
    }

    public Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    public Map<String, Command> getAllCommands() {
        return Collections.unmodifiableMap(commands);
    }

    public void initializeCommands(Scanner scanner,
                                   BackgroundManager backgroundManager,
                                   CharacterManager characterManager,
                                   DialogueManager dialogueManager,
                                   ChoiceManager choiceManager,
                                   GameVariables gameVariables,
                                   AudioManager audioManager) {

        // Регистрируем все команды
        registerCommand(new ChangeBackgroundCommand(scanner, backgroundManager));
        registerCommand(new ShowCharacterCommand(scanner, characterManager));
        registerCommand(new HideCharacterCommand(scanner, characterManager));
        registerCommand(new ChangeSpriteCommand(scanner, characterManager));
        registerCommand(new SayCommand(scanner, dialogueManager));
        registerCommand(new ThinkCommand(scanner, dialogueManager));
        registerCommand(new ChoiceCommand(scanner, choiceManager, gameVariables));
        registerCommand(new SetVariableCommand(scanner, gameVariables));
        registerCommand(new ShowVariablesCommand(scanner, gameVariables));
        registerCommand(new MusicCommand(scanner, audioManager));
        registerCommand(new SoundCommand(scanner, audioManager));
        registerCommand(new StatusCommand(scanner, characterManager, audioManager));
        registerCommand(new HelpCommand(scanner, commands));
    }
}