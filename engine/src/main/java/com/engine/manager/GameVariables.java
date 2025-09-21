package com.engine.manager;

import java.util.HashMap;
import java.util.Map;

public class GameVariables {
    private Map<String, Object> variables = new HashMap<>();

    public void setVariable(String name, Object value) {
        variables.put(name, value);
        System.out.println("Variable set: " + name + " = " + value);
    }

    public Object getVariable(String name) {
        return variables.get(name);
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }

    public void incrementVariable(String name) {
        Object value = variables.get(name);
        if (value instanceof Integer) {
            variables.put(name, (Integer) value + 1);
        } else {
            variables.put(name, 1);
        }
    }

    public void showAllVariables() {
        System.out.println("\nGame Variables:");
        if (variables.isEmpty()) {
            System.out.println("  No variables set");
        } else {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                System.out.println("  " + entry.getKey() + " = " + entry.getValue());
            }
        }
    }

    public Map<String, Object> getAllVariables() {
        return new HashMap<>(variables);
    }

    public void clearAllVariables() {
        variables.clear();
        System.out.println("All variables cleared");
    }
}