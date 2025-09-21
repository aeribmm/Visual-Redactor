package com.engine.commands.base;


public interface Command {
    void execute();
    String getCommandName();
    String getDescription();
}