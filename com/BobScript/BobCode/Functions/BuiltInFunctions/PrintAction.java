package com.BobScript.BobCode.Functions.BuiltInFunctions;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

import java.util.ArrayList;
import java.util.Stack;

public class PrintAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData tmp = info.stack.pop();
        if (tmp.getType() != Type.ARRAY)
            //System.out.println("> " + tmp.getData());
            System.err.println(tmp.getData());
        else {
            System.err.print("[");
            ArrayList<StackData> array = (ArrayList<StackData>)tmp.getData();
            for (int i = 0; i < array.size(); i++) {
                System.err.print(array.get(i).getData());
                if (i != array.size() - 1)
                    System.err.print(", ");
            }
            System.err.println("]");
        }
    }
}
