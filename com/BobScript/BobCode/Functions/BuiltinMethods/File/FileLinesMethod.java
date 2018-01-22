package com.BobScript.BobCode.Functions.BuiltinMethods.File;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.ObjectsFactory;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Type;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

public class FileLinesMethod extends FunctionAction {
    @Override
    public int getArgumentsCount() {
        return 0;
    }

    @Override
    public void Action(InterpreterInfo info) {
        BufferedReader reader = (BufferedReader) info.stack.pop().getData();
        info.stack.push(ObjectsFactory.createIterator(new FileIterator(reader)));
    }

    @Override
    public boolean isBuiltinFunction() {
        return super.isBuiltinFunction();
    }

    private static class FileIterator implements Iterator<StackData> {
        private BufferedReader reader;
        private String nextLine;
        public FileIterator(BufferedReader reader) {
            nextLine = "";
            this.reader = reader;
        }

        @Override
        public boolean hasNext() {
            try {
                nextLine = reader.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return nextLine != null;
        }

        @Override
        public StackData next() {
            if (nextLine != null)
                return ObjectsFactory.createString(nextLine);
            else
                return new StackData(null, Type.NULL);
        }

        @Override
        public void remove() {

        }
    }
}
