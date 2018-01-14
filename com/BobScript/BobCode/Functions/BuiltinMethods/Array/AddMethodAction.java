package com.BobScript.BobCode.Functions.BuiltinMethods.Array;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.Functions.MethodAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;

import java.util.ArrayList;

public class AddMethodAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData obj = info.stack.pop();
        StackData el = info.stack.pop();
        ((ArrayList<StackData>)obj.getData()).add(el);
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
