package com.BobScript.BobCode.Functions.BuiltInFunctions;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;

/**
 * Class for 'print' function
 * prints argument in stdout
 */
public class PrintAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData tmp = info.stack.peek();
        FunctionAction toStr = tmp.getMethod("toStr");
        info.interpreter.callFunction(toStr);
        System.out.println(info.stack.pop().getData());
    }

}
