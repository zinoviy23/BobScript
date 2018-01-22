package com.BobScript.BobCode.Functions.BuiltinMethods.Iterators;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;

import java.util.Iterator;

public class HasNextIteratorMethod extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 0;
    }

    @Override
    public void Action(InterpreterInfo info) {
        Iterator<StackData> it = ((Iterator<StackData>) info.stack.pop().getData());
        info.stack.push(ObjectsFactory.createBoolean(it.hasNext()));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
