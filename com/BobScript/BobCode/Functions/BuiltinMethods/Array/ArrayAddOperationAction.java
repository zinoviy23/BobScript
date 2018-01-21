package com.BobScript.BobCode.Functions.BuiltinMethods.Array;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;

import java.util.ArrayList;


public class ArrayAddOperationAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        ArrayList<StackData> newArray = new ArrayList<>();
        ArrayList<StackData> obj = (ArrayList<StackData>) info.stack.pop().getData();
        ArrayList<StackData> other = (ArrayList<StackData>) info.stack.pop().getData();
        for (StackData el : obj)
            newArray.add(el.clone());
        for (StackData el : other)
            newArray.add(el.clone());
        info.stack.push(ObjectsFactory.createArray(newArray));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
