package com.BobScript.BobCode;

import java.io.Serializable;

public class CommandArray implements Serializable {
    Command[] commands;

    public CommandArray(Command[] commands) {
        this.commands = commands;
    }

    public Command[] getCommands() {
        return commands;
    }

    public void setCommands(Command[] commands) {
        this.commands = commands;
    }
}
