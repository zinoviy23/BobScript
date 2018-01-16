package com.BobScript.BobCode.Functions.BuiltinMethods.Range;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Types.Range;

/**
 * Class for "foreach" method of Range <br>
 *     for all elements in range calls given function
 */
public class ForEachMethodAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData obj = info.stack.pop();
        Range current = (Range)obj.getData();
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
