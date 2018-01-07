package com.BobScript.BobCode.Functions.BuiltinMethods.Function;

import com.BobScript.BobCode.Functions.Function;
import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.Functions.MethodAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;

public class CallMethodAction extends MethodAction {
    private int argumentCount;

    public CallMethodAction(StackData obj) {
        super(obj);
        argumentCount = ((FunctionAction)objectPointer.getData()).getArgumentsCount();
    }

    @Override
    public int getArgumentsCount() {
        return argumentCount;
    }

    @Override
    public void Action(InterpreterInfo info) {
        if (!((FunctionAction)objectPointer.getData()).isBuiltinFunction()) {
            info.functionStack.push(new Function(info.commandIndex, ((FunctionAction) objectPointer.getData())));
            info.functionStackSize++;
        }
        ((FunctionAction)objectPointer.getData()).Action(info);
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
