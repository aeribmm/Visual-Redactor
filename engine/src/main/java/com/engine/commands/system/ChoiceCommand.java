package com.engine.commands.system;

import com.engine.commands.base.BaseCommand;
import com.engine.manager.ChoiceManager;
import com.engine.manager.GameVariables;
import com.engine.manager.ChoiceManager.Choice;
import java.util.Scanner;

public class ChoiceCommand extends BaseCommand {
    private final ChoiceManager choiceManager;
    private final GameVariables gameVariables;

    public ChoiceCommand(Scanner scanner, ChoiceManager choiceManager, GameVariables gameVariables) {
        super(scanner);
        this.choiceManager = choiceManager;
        this.gameVariables = gameVariables;
    }

    @Override
    public void execute() {
        Choice[] choices = {
                new Choice("Подойти к Аири", "approach_airi"),
                new Choice("Остаться на месте", "stay_put"),
                new Choice("Уйти", "leave")
        };

        String result = choiceManager.showChoices(choices);
        System.out.println("Вы выбрали: " + result);
        gameVariables.setVariable("last_choice", result);
    }

    @Override
    public String getCommandName() { return "choice"; }

    @Override
    public String getDescription() { return "choice - Show choice menu"; }
}