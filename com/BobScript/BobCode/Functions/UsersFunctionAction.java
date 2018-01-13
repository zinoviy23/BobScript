package com.BobScript.BobCode.Functions;

import com.BobScript.BobCode.*;
import com.sun.deploy.security.ValidationState;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class UsersFunctionAction extends FunctionAction {
    private int startPosition;
    private ArrayList<ArgumentInfo> argumentsInfo;
    private ArrayList<String> functionsVariables;
    private int startStackSize;

    public static class ArgumentInfo {
        public String name, type;

        public ArgumentInfo(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }


    @Override
    public int getArgumentsCount() {
        return argumentsInfo.size();
    }

    public UsersFunctionAction(int startPosition, ArrayList<ArgumentInfo> argumentsInfo) {
        isBuiltin = false;
        this.startPosition = startPosition;
        this.argumentsInfo = argumentsInfo;
    }

    @Override
    public void Action(InterpreterInfo info) {
        info.commandIndex = startPosition - 1;
        functionsVariables = new ArrayList<>();
        initArguments(info);
        startStackSize = info.stack.size();
    }

    /**
     * Создаёт переменные аргумент
     * @param info информация об состоянии интерпритатора
     */
    private void initArguments(InterpreterInfo info) {
        for (ArgumentInfo arg : argumentsInfo) {
            StackData argumentValue = info.stack.pop();
            Variable argument = new Variable();
            argument.assignCopy(argumentValue);
            if (arg.type == null ||
                    arg.type.equals("Int") && argument.getType() == Type.INT ||
                    arg.type.equals("Float") && argument.getType() == Type.FLOAT ||
                    arg.type.equals("Array") && argument.getType() == Type.ARRAY ||
                    arg.type.equals("String") && argument.getType() == Type.STRING ||
                    arg.type.equals("Boolean") && argument.getType() == Type.BOOLEAN ||
                    arg.type.equals("Function") && argument.getType() == Type.FUNCTION) {
                info.variables.put(info.functionStackSize + "#" + arg.name, argument);
            }
            else {
                Log.printError("ERROR: Wrong argument type! " + arg.name + " : " + arg.type + " and " + argument.getType());
                info.interpreter.errorExit();
                return;
            }

        }
    }

    /**
     * Очищает стек до состояния, в котором он был до вызова
     * @param info
     * @param isReturnSomething
     */
    public void clearStackExit(InterpreterInfo info, boolean isReturnSomething) {
        if (!isReturnSomething)
            while (startStackSize < info.stack.size())
                info.stack.pop();
        else {
            StackData top = info.stack.pop();
            while (startStackSize < info.stack.size())
                info.stack.pop();
            info.stack.push(top);
        }
    }

    /**
     * Завершает выполнение функции
     * @param info информация об состоянии интерпритатора
     *
     */
    public void endFunction(InterpreterInfo info) {
        for (ArgumentInfo arg : argumentsInfo)
            info.variables.remove(info.functionStackSize + "#" + arg.name);
        for (String arg : functionsVariables)
            info.variables.remove(arg);
    }

    public void addVariable(String name) {
        functionsVariables.add(name);
    }


}
