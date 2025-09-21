package com.engine.commands.background;

import com.engine.commands.base.BaseCommand;
import com.engine.manager.bgmanager.BackgroundManager;

import java.util.Scanner;

public class ChangeBackgroundCommand extends BaseCommand {
    private final BackgroundManager backgroundManager;

    public ChangeBackgroundCommand(Scanner scanner, BackgroundManager backgroundManager) {
        super(scanner);
        this.backgroundManager = backgroundManager;
    }

    @Override
    public void execute() {
        System.out.println("Available backgrounds: airi, pres");
        String bgId = readInput("Enter background ID: ");
        backgroundManager.changeBackground(bgId);
    }

    @Override
    public String getCommandName() { return "bg"; }

    @Override
    public String getDescription() { return "bg - Change background"; }
}