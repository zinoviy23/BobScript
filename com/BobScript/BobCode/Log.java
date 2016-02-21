package com.BobScript.BobCode;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.jar.Pack200;

/**
 * Created by zinov on 20.02.2016.
 */
public class Log {
    private static FileWriter log;
    public static void printError(String s) {
        try {
            log.write(s + "\n");
        } catch (Exception ex) {

        }
    }

    public static void init() {
        try {
            log = new FileWriter("Log.txt");
            printError("start: " + new Date(System.currentTimeMillis()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void end() {
        try {
            printError("end :" + new Date(System.currentTimeMillis()));
            log.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
