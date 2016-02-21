package com.BobScript.BobCode;

/**
 * Created by zinov on 20.02.2016.
 */

import java.util.StringTokenizer;

public class Command {
    private Commands cmd;
    private String[] args;

    public Command(int val, String args) {
        this.args = args.split(" ");
        cmd = Commands.toCommands(val);
    }

    public Command(Commands cmd, String args) {
        this.args = args.split(" ");
        this.cmd = cmd;
    }

    Commands getCommand() { return cmd; }
    String[] getArgs() { return args; }

    public static String stringTokenizerToString(StringTokenizer st) {
        String str = "";
        while (st.hasMoreTokens()) {
            str += st.nextToken() + " ";
        }

        return str;
    }



    @Override
    public String toString() {
        String str = "";
        str += cmd.toString() + " ";
        for (int i = 0; i < args.length; i++)
            str += args[i] + " ";

        return str;
    }
}
