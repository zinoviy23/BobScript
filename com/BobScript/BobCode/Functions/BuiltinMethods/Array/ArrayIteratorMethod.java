package com.BobScript.BobCode.Functions.BuiltinMethods.Array;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;

import java.util.ArrayList;

/**
 * Class for "iterator" method of array
 * gets iterator
 */
public class ArrayIteratorMethod extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 0;
    }

    @Override
    public void Action(InterpreterInfo info) {
        ArrayList<StackData> arr = (ArrayList<StackData>) info.stack.pop().getData();
        info.stack.push(ObjectsFactory.createIterator(arr.iterator()));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
