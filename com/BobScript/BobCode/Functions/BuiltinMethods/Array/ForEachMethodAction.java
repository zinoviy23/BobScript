package com.BobScript.BobCode.Functions.BuiltinMethods.Array;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.Functions.MethodAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;

import java.util.ArrayList;

public class ForEachMethodAction extends MethodAction {
    public ForEachMethodAction(StackData obj) {
        super(obj);
    }

    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        ArrayList<StackData> current = ((ArrayList<StackData>) objectPointer.getData());
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
