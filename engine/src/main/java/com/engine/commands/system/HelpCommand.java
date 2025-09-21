package com.engine.commands.system;

import com.engine.commands.base.BaseCommand;
import com.engine.commands.base.Command;

import java.util.Map;
import java.util.Scanner;

public class HelpCommand extends BaseCommand {
    private final Map<String, Command> commands;

    public HelpCommand(Scanner scanner, Map<String, Command> commands) {
        super(scanner);
        this.commands = commands;
    }

    @Override
    public void execute() {
        System.out.println("\n=== Commands ===");
        System.out.println("n - Next text line");

        commands.values().forEach(command ->
                System.out.println(command.getDescription()));

        System.out.println("help - Show this help");
        System.out.println("quit - Exit");
        System.out.println("================");
    }

    @Override
    public String getCommandName() { return "help"; }

    @Override
    public String getDescription() { return "help - Show this help"; }
}