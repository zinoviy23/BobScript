package com.BobScript.Parsing.AbstraxtSyntaxTree;

import java.io.IOException;
import java.io.PrintWriter;

public abstract class TreeNode {
    public abstract void debugPrint(int level);

    private static final String LEVEL = "----";

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

}
