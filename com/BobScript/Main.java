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

    private final static String BOB_SCRIPT_VERSION = "0.1.0";

    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            TreeNode.initDebugWriter("tree.txt");
            TreeParser parser = new TreeParser();
            String fileName = args[0];
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = "";
            PrintWriter tokenWriter = new PrintWriter("tokens.txt");
            FileNode root = new FileNode(fileName);
            do {
                line = reader.readLine();
                if (line == null) {
                    TreeNode tmp = parser.createNode(new Operand("pass"));
                    root.addToBody(tmp);
                    break;
                }
                Operand tmp = new Operand(line);
                tokenWriter.println(tmp);
                tokenWriter.flush();
                TreeNode newTreeNode = parser.createNode(tmp);
                if (newTreeNode != null)
                    root.addToBody(newTreeNode);

            } while (line != null);
            root.debugPrint(0);
            TreeNode.deleteDebugWriter();
            tokenWriter.close();

            Command[] compiled = root.compile();

            FileOutputStream serCommands = new FileOutputStream(fileName + ".bbc");
            ObjectOutputStream oos = new ObjectOutputStream(serCommands);
            oos.writeObject(new CommandArray(compiled));
            oos.flush();
            oos.close();

            FileInputStream fin = new FileInputStream(fileName + ".bbc");
            ObjectInputStream ois = new ObjectInputStream(fin);
            PrintWriter tmpWriter = new PrintWriter(new File("bobcode.bbc"));
            try {
                CommandArray commandArray = (CommandArray) ois.readObject();
                int i = 0;
                for (Command c : commandArray.getCommands()) {
                    tmpWriter.println(i + " : " + c);
                    i++;
                }

            } catch (Exception ex) {
                System.out.println("bliin");
                for (Command c : compiled) {
                    tmpWriter.println(c);
                }
            }

            //for (Command c : compiled) {
              //  tmpWriter.println(c);
            //}
            tmpWriter.close();

            Interpreter inter = new Interpreter();
            Log.init();
            Log.printInfo("BobScript v " + BOB_SCRIPT_VERSION);
            long start = System.currentTimeMillis();
            inter.execute(compiled);
            double time = ((double) System.currentTimeMillis() - start) / 1000;
            Log.printInfo(Double.toString(time));
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

            executeInfo.println("\nStack");
            Stack<StackData> stackData = inter.getStack();
            for (StackData sd : stackData) {
                executeInfo.println(sd);
            }

            executeInfo.close();
        }
        else {
            System.out.println("Enter file name");
        }
    }
}
