package com.BobScript;

import com.BobScript.BobCode.*;
import com.BobScript.Parsing.*;
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
        TreeParser parser = new TreeParser();
        parser.createNode(new Operand("a = (15 * 3.1 + 1 == 10 * (7 - 3))")).debugPrint(0);
    }
}
