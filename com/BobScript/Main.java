package com.BobScript;

import com.BobScript.BobCode.*;
import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.Parsing.*;
import com.BobScript.Parsing.AbstraxtSyntaxTree.FileNode;
import com.BobScript.Parsing.AbstraxtSyntaxTree.TreeNode;
import com.BobScript.Parsing.AbstraxtSyntaxTree.TreeParser;

import java.io.*;
import java.util.*;

public class Main {

    private final static String BOB_SCRIPT_VERSION = "0.0.0";

    public static void main(String[] args) throws IOException {

        if (args.length != 0) {
            TreeNode.initDebugWriter("tree.txt");
            TreeParser parser = new TreeParser();
            String fileName = args[0];
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = "";
            PrintWriter tokenWriter = new PrintWriter("tokens.txt");
            FileNode root = new FileNode(fileName);
            while ((line = reader.readLine()) != null) {
                Operand tmp = new Operand(line);
                tokenWriter.println(tmp);
                tokenWriter.flush();
                TreeNode newTreeNode = parser.createNode(tmp);
                if (newTreeNode != null)
                    root.addToBody(newTreeNode);
            }
            root.debugPrint(0);
            TreeNode.deleteDebugWriter();
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
            double time = ((double) System.currentTimeMillis() - start) / 1000;
            Log.printError(Double.toString(time));
            Log.end();

            PrintWriter executeInfo = new PrintWriter(new File("Info.txt"));

            executeInfo.println("\nVariables: ");
            Set<Map.Entry<String, Variable>> kek = inter.getVariables().entrySet();
            for (Map.Entry<String, Variable> v : kek) {
                executeInfo.println(v.getKey() + " " + v.getValue());
            }

            executeInfo.println("\nFunctions: ");
            Set<Map.Entry<String, FunctionAction>> bob = inter.getFunctions().entrySet();
            for (Map.Entry<String, FunctionAction> f : bob) {
                executeInfo.println(f.getKey() + " " + f.getValue().isBuiltinFunction());
            }
            executeInfo.close();
        }
        else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Interpreter inter = new Interpreter();
            TreeParser parser = new TreeParser();
            boolean exit = false;
            boolean prev = false;
            System.out.println("BobScript Test" + BOB_SCRIPT_VERSION);
            do {
                if (!prev)
                    System.out.print("\n>> ");
                String line = reader.readLine();
                if (line.equals("exit"))
                    exit = true;
                else {
                    Operand op = new Operand(line);
                    TreeNode currentNode = parser.createNode(op);
                    if (currentNode == null) {
                        prev = true;
                        continue;
                    }
                    if (prev)
                        prev = false;
                    inter.execute(currentNode.compile());
                    if (!inter.getStack().empty())
                        System.out.println(inter.getStack().pop());
                }
            } while (!exit);
        }
    }
}
