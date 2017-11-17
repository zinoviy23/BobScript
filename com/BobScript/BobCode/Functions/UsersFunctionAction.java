package com.BobScript.BobCode.Functions;

import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;
import com.BobScript.BobCode.Variable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class UsersFunctionAction extends FunctionAction {
    private int startPosition;
    private ArrayList<String> argumentsInfo;
    private ArrayList<String> functionsVariables;

    @Override
    public int getArgumentsCount() {
        return argumentsInfo.size();
    }

    public UsersFunctionAction(int startPosition, ArrayList<String> argumentsInfo) {
        isBuiltin = false;
        this.startPosition = startPosition;
        this.argumentsInfo = argumentsInfo;
    }

    @Override
    public void Action(InterpreterInfo info) {
        info.commandIndex = startPosition - 1;
        functionsVariables = new ArrayList<>();
        initArguments(info);
    }

    /**
     * Создаёт переменные аргумент
     * @param info информация об состоянии интерпритатора
     */
    private void initArguments(InterpreterInfo info) {
        for (String arg : argumentsInfo) {
            StackData argumentValue = info.stack.pop();
            Variable argument = new Variable();
            argument.assignCopy(argumentValue);

            info.variables.put(info.functionStackSize + "#" + arg, argument);
        }
    }

    /**
     * Завершает выполнение функции
     * @param info информация об состоянии интерпритатора
     *
     */
    public void endFunction(InterpreterInfo info) {
        for (String arg : argumentsInfo)
            info.variables.remove(info.functionStackSize + "#" + arg);
        for (String arg : functionsVariables)
            info.variables.remove(arg);
    }

    public void addVariable(String name) {
        functionsVariables.add(name);
    }


}
