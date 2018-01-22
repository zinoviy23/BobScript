package com.BobScript.BobCode.Functions.BuiltinMethods.Iterators;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;

import java.util.Iterator;

public class NextIteratorMethod extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 0;
    }

    @Override
    public void Action(InterpreterInfo info) {
        Iterator<StackData> it = ((Iterator<StackData>) info.stack.pop().getData());
        info.stack.push(it.next());
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
