package com.BobScript.BobCode.Functions.BuiltinMethods;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

import java.util.ArrayList;

/**
 * Class for "toStr" method of Object <br>
 *     convert object to string
 */
public class ToStrMethodAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 0;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData obj = info.stack.pop();
        info.stack.push(ObjectsFactory.createString(ObjectToString(obj)));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }

    /**
     * Convert given object to string
     * @param obj object
     * @return string, that represent object
     */
    public static String ObjectToString(StackData obj) {
        if (obj.getType() == Type.USERS) {
            return "<USERS OBJECT>";
        }
        if (obj.getType() != Type.ARRAY) {
            if (obj.getType() != Type.NULL)
                return obj.getData().toString();
            else
                return "null";
        } else {
            ArrayList<StackData> currentArray = (ArrayList<StackData>) obj.getData();
            StringBuilder stringResult = new StringBuilder("[");
            for (int i = 0; i < currentArray.size(); i++) {
                stringResult.append(ObjectToString(currentArray.get(i)));
                if (i != currentArray.size() - 1) {
                    stringResult.append(", ");
                }
            }
            stringResult.append("]");
            return stringResult.toString();
        }
    }
}
