package com.engine.commands.system;

import com.engine.commands.base.BaseCommand;
import com.engine.manager.GameVariables;

import java.util.Scanner;

public class SetVariableCommand extends BaseCommand {
    private final GameVariables gameVariables;

    public SetVariableCommand(Scanner scanner, GameVariables gameVariables) {
        super(scanner);
        this.gameVariables = gameVariables;
    }

    @Override
    public void execute() {
        String name = readInput("Enter variable name: ");
        String value = readInput("Enter value: ");

        // Пытаемся распарсить как число
        try {
            int intValue = Integer.parseInt(value);
            gameVariables.setVariable(name, intValue);
        } catch (NumberFormatException e) {
            gameVariables.setVariable(name, value);
        }
    }

    @Override
    public String getCommandName() { return "set"; }

    @Override
    public String getDescription() { return "set - Set game variable"; }
}