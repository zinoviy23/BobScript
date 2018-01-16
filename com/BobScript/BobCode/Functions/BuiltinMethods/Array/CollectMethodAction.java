package com.BobScript.BobCode.Functions.BuiltinMethods.Array;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;

import java.util.ArrayList;

/**
 * Class for collect method of array <br>
 *     gets function-predicate and returns array of elements, that satisfy it
 */
public class CollectMethodAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        ArrayList<StackData> array = (ArrayList<StackData>)info.stack.pop().getData();
        ArrayList<StackData> newArray = new ArrayList<>();
        FunctionAction func = (FunctionAction)info.stack.pop().getData();

        for (StackData el : array) {
            info.stack.push(el);
            info.interpreter.callFunction(func);
            boolean result = (boolean)info.stack.pop().getData();
            if (result) {
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
