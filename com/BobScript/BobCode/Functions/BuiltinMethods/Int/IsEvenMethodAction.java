package com.BobScript.BobCode.Functions.BuiltinMethods.Int;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

/**
 * Class for <i>even?</i> method of Int
 */
public class IsEvenMethodAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 0;
    }

    @Override
    public void Action(InterpreterInfo info) {
        long objValue = (long)info.stack.pop().getData();
        info.stack.push(ObjectsFactory.createBoolean(objValue % 2 == 0));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
