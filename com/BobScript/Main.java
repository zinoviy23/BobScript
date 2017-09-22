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
        /*System.out.println(Arrays.toString(args));
        BufferedReader reader = new BufferedReader(new FileReader("kek.txt"));
        String line = "";
        Parser compiler = new Parser();
        PrintWriter tokenWriter = new PrintWriter("tokens.txt");
        while ((line = reader.readLine()) != null) {
            tokenWriter.println(new Operand(line));
            compiler.compile(new Operand(line));
        }
        tokenWriter.close();

        PrintWriter tmpWriter = new PrintWriter(new File("bobcode.bbc"));
        Command[] cmd = compiler.getProgram();
        for (Command c : cmd) {
            tmpWriter.println(c);
        }
        tmpWriter.close();

        Interpreter inter = new Interpreter();
        Log.init();
        Log.printError("BobScript v " + BOB_SCRIPT_VERSION);
        long start = System.currentTimeMillis();
        inter.execute(cmd);
        double time = ((double)System.currentTimeMillis() - start) / 1000;
        Log.printError(Double.toString(time));
        Log.end();

        System.out.println("\nVariables: ");
        Set <Map.Entry<String, Variable>> kek = inter.getVariables().entrySet();
        for (Map.Entry<String, Variable> v: kek) {
            System.out.println(v.getKey() + " " + v.getValue());
        }*/
        TreeNode.initDebugWriter("tree.txt");
        TreeParser parser = new TreeParser();
        /*parser.createNode(new Operand("while a < 10"));
        parser.createNode(new Operand("b = 10 * 3 + 1 - 2"));
        parser.createNode(new Operand("c = 10 < 5 + 345 == 50.1"));
        parser.createNode(new Operand("if a == 5"));
        parser.createNode(new Operand("a = 6"));
        parser.createNode(new Operand("end"));
        parser.createNode(new Operand("while c < 0"));
        parser.createNode(new Operand("c = 10"));
        parser.createNode(new Operand("end"));
        parser.createNode(new Operand("end")).debugPrint(0);
        //parser.createNode(new Operand("a, b, c, d + 1")).debugPrint(0); */

        BufferedReader reader = new BufferedReader(new FileReader("kek.txt"));
        String line = "";
        PrintWriter tokenWriter = new PrintWriter("tokens.txt");
        FileNode root = new FileNode("kek.txt");
        while ((line = reader.readLine()) != null) {
            Operand tmp = new Operand(line);
            tokenWriter.println(tmp);
            TreeNode newTreeNode = parser.createNode(tmp);
            if (newTreeNode != null)
                root.addToBody(newTreeNode);
        }
        root.debugPrint(0);
        tokenWriter.close();
        TreeNode.deleteDebugWriter();

    }
}
