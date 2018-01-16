package com.BobScript.BobCode.Functions.BuiltInFunctions;

import com.BobScript.BobCode.Functions.BuiltinMethods.ToStrMethodAction;
import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Variable;

import java.util.Map;
import java.util.Set;

public class PrintAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData tmp = info.stack.pop();
        System.out.print(ToStrMethodAction.ObjectToString(tmp));
    }

}
