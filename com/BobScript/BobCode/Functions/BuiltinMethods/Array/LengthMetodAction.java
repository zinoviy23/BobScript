package com.BobScript.BobCode.Functions.BuiltinMethods.Array;

import com.BobScript.BobCode.Functions.MethodAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

import java.util.ArrayList;

public class LengthMetodAction extends MethodAction {
    public LengthMetodAction(StackData obj) {
        super(obj);
    }

    @Override
    public int getArgumentsCount() {
        return 0;
    }

    @Override
    public void Action(InterpreterInfo info) {
        long len = ((ArrayList<StackData>)objectPointer.getData()).size();
        info.stack.push(new StackData(len, Type.INT));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
