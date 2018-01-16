package com.BobScript.BobCode.Functions.BuiltInFunctions;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

import java.util.Random;

/**
 * Class for "random" function <br>
 *     gets two Ints and crete random Int between them
 */
public class RandomAction extends FunctionAction {
    private Random rand = new Random();

    @Override
    public int getArgumentsCount() {
        return 2;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData l = info.stack.pop();
        StackData r = info.stack.pop();
        long lValue = (long)l.getData();
        long rValue = (long)r.getData();
        info.stack.push(new StackData(Math.abs(rand.nextLong()) % (rValue - lValue) + lValue, Type.INT));
    }
}
