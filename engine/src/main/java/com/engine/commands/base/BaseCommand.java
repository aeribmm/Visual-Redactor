package com.engine.commands.base;
import java.util.Scanner;

public abstract class BaseCommand implements Command {
    protected Scanner scanner;

    public BaseCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    protected String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    protected String readInput(String prompt, String defaultValue) {
        System.out.print(prompt + " [" + defaultValue + "]: ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }
}