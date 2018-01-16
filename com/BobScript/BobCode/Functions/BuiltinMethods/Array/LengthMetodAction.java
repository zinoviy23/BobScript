package com.BobScript.BobCode.Functions.BuiltinMethods.Array;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

import java.util.ArrayList;

/**
 * Class for "length" method of array <br>
 *     returns length of array
 */
public class LengthMetodAction extends FunctionAction {

    @Override
    public int getArgumentsCount() {
        return 0;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData obj = info.stack.pop();
        long len = ((ArrayList<StackData>)obj.getData()).size();
        info.stack.push(new StackData(len, Type.INT));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
