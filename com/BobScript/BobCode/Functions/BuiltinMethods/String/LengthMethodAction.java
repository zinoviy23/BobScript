package com.BobScript.BobCode.Functions.BuiltinMethods.String;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.Functions.MethodAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

public class LengthMethodAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 0;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData obj = info.stack.pop();
        long len = ((String)obj.getData()).length();
        info.stack.push(new StackData(len, Type.INT));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
