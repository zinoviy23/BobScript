package com.BobScript.BobCode.Functions.BuiltinMethods.String;

import com.BobScript.BobCode.Functions.MethodAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

import java.util.ArrayList;

public class SplitMethodAction extends MethodAction {
    public SplitMethodAction(StackData obj) {
        super(obj);
    }

    @Override
    public int getArgumentsCount() {
        return 1;
    }

    @Override
    public void Action(InterpreterInfo info) {
        StackData regexObj = info.stack.pop();
        if (regexObj.getType() != Type.STRING)
            return;
        String[] res = ((String)objectPointer.getData()).split((String)regexObj.getData());
        ArrayList<StackData> data = new ArrayList<>();
        for (String r : res) {
            data.add(ObjectsFactory.createString(r));
        }
        info.stack.push(ObjectsFactory.createArray(data));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
