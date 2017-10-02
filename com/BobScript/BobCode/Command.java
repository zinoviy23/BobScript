package com.BobScript.BobCode;

/**
 * Created by zinov on 20.02.2016.
 */

import java.util.StringTokenizer;

public class Command {
    private Commands cmd;
    private Object[] args;

    public Command(Commands cmd, Object... params) {
        this.cmd = cmd;
        args = params;
    }

    Commands getCommand() { return cmd; }
    Object[] getArgs() { return args; }

    @Deprecated
    public static String stringTokenizerToString(StringTokenizer st) {
        String str = "";
        while (st.hasMoreTokens()) {
            str += st.nextToken() + " ";
        }

        return str;
    }



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
