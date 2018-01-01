package com.BobScript.BobCode.Functions.BuiltInFunctions;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

import java.util.ArrayList;
import java.util.Stack;

public class PrintAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData tmp = info.stack.pop();
        print(tmp);
    }

    /**
     * Выводит, с учетом массивов и тд
     * @param data
     */
    private void print(StackData data) {
        if (data.getType() != Type.ARRAY) {
            System.out.print(data.getData());
        } else {
            ArrayList<StackData> arrayOfData = (ArrayList<StackData>)data.getData();
            System.out.print("[");
            for (int i = 0; i < arrayOfData.size(); i++) {
                print(arrayOfData.get(i));
                if (i != arrayOfData.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("]");
        }
    }
}
