package com.engine.commands.dialogue;
import com.engine.commands.base.BaseCommand;
import com.engine.manager.DialogueManager;

import java.util.Scanner;

public class ThinkCommand extends BaseCommand {
    private final DialogueManager dialogueManager;

    public ThinkCommand(Scanner scanner, DialogueManager dialogueManager) {
        super(scanner);
        this.dialogueManager = dialogueManager;
    }

    @Override
    public void execute() {
        String charId = readInput("Enter character ID: ");
        String text = readInput("Enter thought text: ");
        dialogueManager.showThought(charId, text);
    }

    @Override
    public String getCommandName() { return "think"; }

    @Override
    public String getDescription() { return "think - Character thought"; }
}