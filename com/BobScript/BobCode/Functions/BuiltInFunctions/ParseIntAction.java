package com.BobScript.BobCode.Functions.BuiltInFunctions;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

/**
 * Class for parseInt function <br>
 *     gets string and convert it to <b>Int</b>
 */
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
            info.stack.push(ObjectsFactory.createInt(Long.parseLong((String)st.getData())));
        }
    }
}
