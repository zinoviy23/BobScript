package com.BobScript.BobCode.Functions.BuiltInFunctions;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.Variable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class for "open" function <br>
 *     open file and read it or write at it
 */
public class OpenFunctionAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 3;
    }

    @Override
    public void Action(InterpreterInfo info) {
        String fileName = ((String) info.stack.pop().getData());
        String param = ((String) info.stack.pop().getData());
        FunctionAction action = ((FunctionAction) info.stack.pop().getData());

        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
            info.stack.push(ObjectsFactory.createFile(fileReader, fileName));
            info.interpreter.callFunction(action);
            fileReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            info.interpreter.errorExit();
        }
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }
}
