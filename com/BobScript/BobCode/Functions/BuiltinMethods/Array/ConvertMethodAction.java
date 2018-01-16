package com.BobScript.BobCode.Functions.BuiltinMethods.Array;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;

import java.util.ArrayList;

/**
 * Class for convert method of array <br>
 *     gets function and convert array elements with it
 */
public class ConvertMethodAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData obj = info.stack.pop();
        ArrayList<StackData> current = (ArrayList<StackData>)obj.getData();
        ArrayList<StackData> newArray = new ArrayList<>();

        FunctionAction func = (FunctionAction) info.stack.pop().getData();

        for (StackData el: current) {
            info.stack.push(el);
            info.interpreter.callFunction(func);
            newArray.add(info.stack.pop());
        }

        info.stack.push(ObjectsFactory.createArray(newArray));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
