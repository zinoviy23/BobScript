package com.BobScript.BobCode;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * Class for representation commands <br>
 * It is serializable
 */
public class Command implements Serializable {
    private Commands cmd;
    private Object[] args;

    /**
     * Standard constructor
     * @param cmd Command
     * @param params Command parameters
     */
    public Command(Commands cmd, Object... params) {
        this.cmd = cmd;
        args = params;
    }

    /**
     * Gets command
     * @return command
     */
    public Commands getCommand() { return cmd; }

    /**
     * Gets parameters
     * @return parameters
     */
    public Object[] getArgs() { return args; }

    /**
     * Convert string tokenizer to String
     * @param st String tokenizer
     * @return string value
     */
    @Deprecated
    public static String stringTokenizerToString(StringTokenizer st) {
        String str = "";
        while (st.hasMoreTokens()) {
            str += st.nextToken() + " ";
        }

        return str;
    }

    /**
     * Overrided toString method
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append(cmd).append(" ");
        if (args != null)
            for (int i = 0; i < args.length; i++)
                sb.append(args[i]).append(" ");

        return sb.toString();
    }
}
