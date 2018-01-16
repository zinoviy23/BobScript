package com.BobScript.BobCode.Functions.BuiltInFunctions;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

import java.util.ArrayList;

/**
 * Class for function "createArray" <br>
 * create array of null and with given length
 */
@Deprecated
public class CreateArrayFunctionAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData st = info.stack.pop();
        if (st.getType() != Type.INT)
            return;
        else {
            long size = (long)st.getData();
            ArrayList<StackData> newArray = new ArrayList<>();
            newArray.ensureCapacity((int)size);
            for (int i = 0; i < size; i++)
                newArray.add(new StackData(null, Type.NULL));
            info.stack.push(new StackData(newArray, Type.ARRAY));
        }
    }
}
