package com.engine.manager;

import java.util.Scanner;

public class ChoiceManager {
    private Scanner scanner = new Scanner(System.in);

    public String showChoices(Choice[] choices) {
        System.out.println("\nВыберите действие:");

        for (int i = 0; i < choices.length; i++) {
            System.out.println((i + 1) + ". " + choices[i].getText());
        }

        while (true) {
            System.out.print("Ваш выбор (1-" + choices.length + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= choices.length) {
                    return choices[choice - 1].getResult();
                } else {
                    System.out.println("Неверный выбор!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
            }
        }
    }

    // Внутренний класс Choice
    public static class Choice {
        private String text;
        private String result;
        private String condition;

        public Choice(String text, String result) {
            this.text = text;
            this.result = result;
        }

        public Choice(String text, String result, String condition) {
            this(text, result);
            this.condition = condition;
        }

        public String getText() { return text; }
        public String getResult() { return result; }
        public String getCondition() { return condition; }
    }
}