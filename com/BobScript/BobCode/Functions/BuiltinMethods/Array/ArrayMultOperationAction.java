package com.BobScript.BobCode.Functions.BuiltinMethods.Array;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;

import java.util.ArrayList;

public class ArrayMultOperationAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        ArrayList<StackData> obj = (ArrayList<StackData>) info.stack.pop().getData();
        long other = (long) info.stack.pop().getData();

        ArrayList<StackData> newArray = new ArrayList<>();
        for (int i = 0; i < other; i++) {
            for (StackData el : obj) {
                newArray.add(el.clone());
            }
        }
        info.stack.push(ObjectsFactory.createArray(newArray));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
