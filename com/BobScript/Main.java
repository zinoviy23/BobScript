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

        Command[] program = {new Command(Commands.PUSH, "\"10\""),
                new Command(Commands.PUSH, "10.2"), new Command(Commands.ADD, "")};
        long start = System.currentTimeMillis();
        Log.printError(Integer.toString(inter.execute(program)));
        long end = System.currentTimeMillis();
        Log.printError(Double.toString(((double)end - start) / 1000));
        Log.end();
        Stack<StackData> st = inter.getStack();
        for (Object sd : st.toArray()) {
            System.out.println(sd);
        }

        Map <String, Varible> map = inter.getVaribles();
        Iterator<Map.Entry<String, Varible>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Varible> nxt = it.next();
            System.out.println(nxt.getKey() + " as " + nxt.getValue());
        }
    }
}
