package com.engine.commands.system;

import com.engine.commands.base.BaseCommand;
import com.engine.manager.GameVariables;

import java.util.Scanner;

public class ShowVariablesCommand extends BaseCommand {
    private final GameVariables gameVariables;

    public ShowVariablesCommand(Scanner scanner, GameVariables gameVariables) {
        super(scanner);
        this.gameVariables = gameVariables;
    }

    @Override
    public void execute() {
        gameVariables.showAllVariables();
    }

    @Override
    public String getCommandName() { return "vars"; }

    @Override
    public String getDescription() { return "vars - Show all variables"; }
}