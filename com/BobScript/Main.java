package com.BobScript;

import com.BobScript.BobCode.*;
import com.BobScript.Parsing.*;
import com.BobScript.Parsing.AbstraxtSyntaxTree.FileNode;
import com.BobScript.Parsing.AbstraxtSyntaxTree.TreeNode;
import com.BobScript.Parsing.AbstraxtSyntaxTree.TreeParser;

import java.io.*;
import java.util.*;

public class Main {

    final static String BOB_SCRIPT_VERSION = "0.0.0";

    public static void main(String[] args) throws IOException {

        TreeNode.initDebugWriter("tree.txt");
        TreeParser parser = new TreeParser();

        BufferedReader reader = new BufferedReader(new FileReader("kek.txt"));
        String line = "";
        PrintWriter tokenWriter = new PrintWriter("tokens.txt");
        FileNode root = new FileNode("kek.txt");
        while ((line = reader.readLine()) != null) {
            Operand tmp = new Operand(line);
            tokenWriter.println(tmp);
            tokenWriter.flush();
            TreeNode newTreeNode = parser.createNode(tmp);
            if (newTreeNode != null)
                root.addToBody(newTreeNode);
        }
        root.debugPrint(0);
        tokenWriter.close();

        Command[] compiled = root.compile();

        PrintWriter tmpWriter = new PrintWriter(new File("bobcode.bbc"));
        for (Command c : compiled) {
            tmpWriter.println(c);
        }
        tmpWriter.close();

        Interpreter inter = new Interpreter();
        Log.init();
        Log.printError("BobScript v " + BOB_SCRIPT_VERSION);
        long start = System.currentTimeMillis();
        inter.execute(compiled);
        double time = ((double)System.currentTimeMillis() - start) / 1000;
        Log.printError(Double.toString(time));
        Log.end();

        System.out.println("\nVariables: ");
        Set <Map.Entry<String, Variable>> kek = inter.getVariables().entrySet();
        for (Map.Entry<String, Variable> v: kek) {
            System.out.println(v.getKey() + " " + v.getValue());
        }

        TreeNode.deleteDebugWriter();
    }
}
