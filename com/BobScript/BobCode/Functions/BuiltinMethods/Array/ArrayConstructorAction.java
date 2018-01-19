package com.BobScript.BobCode.Functions.BuiltinMethods.Array;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;

import java.util.ArrayList;

/**
 * Class for array constructor method
 */
public class ArrayConstructorAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 2;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData obj = info.stack.pop();
        long size = (long) info.stack.pop().getData();
        FunctionAction action = (FunctionAction) info.stack.pop().getData();

        ArrayList<StackData> arrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            info.stack.push(ObjectsFactory.createInt(i));
            info.interpreter.callFunction(action);
            arrayList.add(info.stack.pop());
        }
        obj.setData(arrayList);
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
