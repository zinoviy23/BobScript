package com.BobScript.BobCode.Functions.BuiltinMethods.Range;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Types.Range;

import java.util.ArrayList;

public class RangeIteratorMethod extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 0;
    }

    @Override
    public void Action(InterpreterInfo info) {
        Range range = (Range) info.stack.pop().getData();
        info.stack.push(ObjectsFactory.createIterator(range.iterator()));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
