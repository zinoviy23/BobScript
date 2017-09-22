package com.BobScript.Parsing.AbstraxtSyntaxTree;

import com.BobScript.BobCode.Command;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public abstract class TreeNode {
    public abstract void debugPrint(int level);

    private static final String LEVEL = "----";

    public abstract Command[] compile();

    protected static PrintWriter debugWriter;

    public static void drawLevel(int level) {
        if (level == 0)
            return;

        for (int i = 0; i < level; i++)
            debugWriter.print(LEVEL);
        debugWriter.print("| ");
    }

    public static void initDebugWriter(String fileName) {
        try {
            debugWriter = new PrintWriter(fileName);
        } catch (IOException exception) {
            exception.printStackTrace();
            debugWriter = new PrintWriter(System.out);
        }
    }

    public static void deleteDebugWriter() {
        if (debugWriter != null)
            debugWriter.close();
    }

    public static Command[] arrayListToArray(ArrayList<Command> arrayList) {
        Command[] ret = new Command[arrayList.size()];

        for (int i = 0; i < arrayList.size(); i++)
            ret[i] = arrayList.get(i);

        return ret;
    }

}
