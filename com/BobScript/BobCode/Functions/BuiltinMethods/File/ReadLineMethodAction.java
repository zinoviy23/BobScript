package com.BobScript.BobCode.Functions.BuiltinMethods.File;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Class for "readLine" method of File <br>
 *     read one line from file
 */
public class ReadLineMethodAction extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 0;
    }

    @Override
    public void Action(InterpreterInfo info) {
        BufferedReader fileReader = ((BufferedReader) info.stack.pop().getData());
        try {
            String line = fileReader.readLine();
            if (line != null)
                info.stack.push(ObjectsFactory.createString(line));
            else
                info.stack.push(new StackData(null, Type.NULL));
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
