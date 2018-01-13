package com.BobScript.BobCode;

import com.BobScript.BobCode.Functions.Function;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Stack;
import java.util.jar.Pack200;

/**
 * Created by zinov on 20.02.2016.
 */
public class Log {
    private static FileWriter log;
    private static InterpreterInfo info;

    public static InterpreterInfo getInfo() {
        return info;
    }

    public static void setInfo(InterpreterInfo info) {
        Log.info = info;
    }

    private static String genFunctionStackInfo() {
        StringBuilder res = new StringBuilder();
        Stack<Function> stack = info.functionStack;
        Enumeration<Function> it = stack.elements();
        while (it.hasMoreElements()) {
            Function f = it.nextElement();
            res.append("in ").append(f.name).append("\n");
        }
        return res.toString();
    }

    public static void printError(String s) {
        try {
            log.write(s + "\n");
            System.err.println(genFunctionStackInfo() + s);
        } catch (Exception ex) {

        }
    }

    public static void printInfo(String s) {
        try {
            log.write(s + "\n");
        } catch (Exception ex) {

        }
    }

    public static void init() {
        try {
            log = new FileWriter("Log.txt");
            log.write("start: " + new Date(System.currentTimeMillis()) + "\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void end() {
        try {
            log.write("end :" + new Date(System.currentTimeMillis()) + "\n");
            log.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
