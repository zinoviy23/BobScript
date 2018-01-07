package com.BobScript.BobCode.Functions.BuiltinMethods.String;

import com.BobScript.BobCode.Functions.MethodAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;

public class SubstringMethodAction extends MethodAction {
    public SubstringMethodAction(StackData obj) {
        super(obj);
    }

    @Override
    public int getArgumentsCount() {
        return 2;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData left = info.stack.pop();
        StackData right = info.stack.pop();

        long leftInd = (long)left.getData();
        long rightInd = (long)right.getData();
        String res = ((String)objectPointer.getData()).substring((int)leftInd, (int)rightInd);

        info.stack.push(ObjectsFactory.createString(res));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
