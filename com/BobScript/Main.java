package com.BobScript;

import com.BobScript.BobCode.Interpriter;
import com.BobScript.BobCode.Log;
import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Varible;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Interpriter inter = new Interpriter();
        Log.init();

        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        Log.printError(Double.toString(((double)end - start) / 1000));
        Log.end();
    }
}
