package com.BobScript.BobCode.Functions;

import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Variable;

import java.util.ArrayList;
import java.util.Stack;

public class UsersFunctionAction extends FunctionAction {
    private Function functionInfo;
    private ArrayList<String> variables;

    public UsersFunctionAction(Function functionInfo) {
        this.functionInfo = functionInfo;
    }

    @Override
    public int getArgumentsCount() {
        return functionInfo.getArguments().size();
    }

    @Override
    public void Action(InterpreterInfo info) {
        info.commandIndex = functionInfo.getStartPosition();
        createArgumentsVariables(info);
    }

    public Function getFunctionInfo() {
        return functionInfo;
    }

    private void createArgumentsVariables(InterpreterInfo info) {
        for (String arg : functionInfo.getArguments()) {
            Variable tmp = new Variable();
            tmp.assignCopy(info.stack.pop());
            info.variables.put(arg, tmp);
        }
    }

    public void removeLocalVariables(InterpreterInfo info) {
        for (String arg : functionInfo.getArguments())
            info.variables.remove(arg);
    }
}
