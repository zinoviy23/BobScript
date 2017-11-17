package com.BobScript.BobCode.Functions.BuiltInFunctions;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

public class ParseIntAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData st = info.stack.pop();
        if (st.getType() != Type.STRING)
            return;
        else {
            info.stack.push(new StackData(Long.parseLong((String)st.getData()),Type.INT));
        }
    }
}
