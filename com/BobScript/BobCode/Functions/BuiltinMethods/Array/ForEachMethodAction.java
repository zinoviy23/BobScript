package com.BobScript.BobCode.Functions.BuiltinMethods.Array;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;

import java.util.ArrayList;

/**
 * Class for "foreach" method of array <br>
 *     gets function and for each element call if with current element
 */
public class ForEachMethodAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData obj = info.stack.pop();
        ArrayList<StackData> current = ((ArrayList<StackData>) obj.getData());
        FunctionAction func = (FunctionAction)info.stack.pop().getData();
        for (StackData el : current) {
            info.stack.push(el);
            info.interpreter.callFunction(func);
        }
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
