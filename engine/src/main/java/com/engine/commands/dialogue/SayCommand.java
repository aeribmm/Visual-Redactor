package com.engine.commands.dialogue;
import com.engine.commands.base.BaseCommand;
import com.engine.manager.DialogueManager;
import java.util.Scanner;
public class SayCommand extends BaseCommand {
    private final DialogueManager dialogueManager;

    public SayCommand(Scanner scanner, DialogueManager dialogueManager) {
        super(scanner);
        this.dialogueManager = dialogueManager;
    }

    @Override
    public void execute() {
        String charId = readInput("Enter character ID: ");
        String text = readInput("Enter dialogue text: ");
        dialogueManager.showDialogue(charId, text);
    }

    @Override
    public String getCommandName() { return "say"; }

    @Override
    public String getDescription() { return "say - Make character speak"; }
}