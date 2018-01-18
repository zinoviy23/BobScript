package com.BobScript.BobCode;

import java.io.Serializable;

/**
 * Class for commands array
 */
public class CommandArray implements Serializable {
    private Command[] commands;

    /**
     * Constructor
     * @param commands array of commands
     */
    public CommandArray(Command[] commands) {
        this.commands = commands;
    }

    /**
     * Gets commands array
     * @return commands
     */
    public Command[] getCommands() {
        return commands;
    }

    /**
     * Sets commands array
     * @param commands new commands value
     */
    public void setCommands(Command[] commands) {
        this.commands = commands;
    }
}
