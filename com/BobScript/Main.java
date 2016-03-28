package com.BobScript;

import com.BobScript.BobCode.*;
import com.BobScript.Parsing.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    final static String BOB_SCRIPT_VERSION = "0.0.0";

    public static void main(String[] args) throws IOException {
        System.out.println(Arrays.toString(args));
        BufferedReader reader = new BufferedReader(new FileReader("kek.txt"));
        String line = "";
        Parser compiler = new Parser();
        while ((line = reader.readLine()) != null) {
            System.out.println(new Operand(line));
            compiler.compile(new Operand(line));
        }
        Command[] cmd = compiler.getProgram();
        for (Command c : cmd) {
            System.out.println(c);
        }
        /*Interpreter inter = new Interpreter();
        Log.init();
        Log.printError("BobScript v " + BOB_SCRIPT_VERSION);
        long start = System.currentTimeMillis();
        inter.execute(cmd);
        double time = ((double)System.currentTimeMillis() - start) / 1000;
        Log.printError(Double.toString(time));
        Log.end();
        Set <Map.Entry<String, Variable>> kek = inter.getVariables().entrySet();
        Iterator<Map.Entry<String, Variable>> it = kek.iterator();

        while (it.hasNext()) {
            Map.Entry<String, Variable> v = it.next();
            System.out.println(v.getKey() + " " + v.getValue());

        }*/
    }
}
