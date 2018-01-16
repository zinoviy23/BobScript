package com.BobScript.BobCode.Functions.BuiltinMethods.String;

import com.BobScript.BobCode.Functions.BuiltinMethods.ToStrMethodAction;
import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;

import java.util.ArrayList;

/**
 * Class for "join" method of string <br>
 *     converts given array to string and separates elements with this object
 */
public class JoinMethodAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        String delimiter = (String) info.stack.pop().getData();
        StringBuilder stringResult = new StringBuilder();
        ArrayList<StackData> currentArray = (ArrayList<StackData>) info.stack.pop().getData();
        for (int i = 0; i < currentArray.size(); i++) {
            stringResult.append(ToStrMethodAction.ObjectToString(currentArray.get(i)));
            if (i != currentArray.size() - 1)
                stringResult.append(delimiter);
        }

        info.stack.push(ObjectsFactory.createString(stringResult.toString()));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
