package com.BobScript.BobCode.Functions;

import com.BobScript.BobCode.*;

import java.util.ArrayList;

/**
 * Class for user function actions
 */
public class UsersFunctionAction extends FunctionAction {
    private int startPosition;
    private ArrayList<ArgumentInfo> argumentsInfo;
    private ArgumentInfo thisInfo = null;

    private int startStackSize;
    public int parentFunctionStackSize;

    /**
     * Class for information about argument
     */
    public static class ArgumentInfo {
        /**
         * name parameter
         */
        public String name;
        /**
         * type parameter
         */
        public String type;

        /**
         * Constructor
         * @param name argument name
         * @param type argument type
         */
        public ArgumentInfo(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    @Override
    public int getArgumentsCount() {
        return argumentsInfo.size();
    }

    /**
     * Standard constructor
     * @param startPosition first body element index
     * @param argumentsInfo information about arguments
     * @param parentFunctionStackSize index in size of function, that it was declared in
     */
    public UsersFunctionAction(int startPosition, ArrayList<ArgumentInfo> argumentsInfo, int parentFunctionStackSize) {
        isBuiltin = false;
        this.startPosition = startPosition;
        this.argumentsInfo = argumentsInfo;
        this.parentFunctionStackSize = parentFunctionStackSize;
    }

    public UsersFunctionAction(int startPosition, ArrayList<ArgumentInfo> argumentsInfo, int parentFunctionStackSize, boolean isMethod) {
        this(startPosition, argumentsInfo, parentFunctionStackSize);
        thisInfo = new ArgumentInfo("this",null);
    }

    @Override
    public void Action(InterpreterInfo info) {
        info.commandIndex = startPosition - 1;
        initArguments(info);
        startStackSize = info.stack.size();
    }

    /**
     * Create argument variables
     * @param info interpreter info
     */
    private void initArguments(InterpreterInfo info) {
        if (thisInfo != null) {
            StackData argumentValue = info.stack.pop();
            Variable argument = new Variable();
            argument.assignCopy(argumentValue);
            info.variables.put(info.functionStackSize + "#" + thisInfo.name, argument);
        }
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
     * Clears stack to position, that it was before calling
     * @param info interpreter information
     * @param isReturnSomething true if function returns something and false else
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
     * Stops function executing
     * @param info interpreter info
     *
     */
    public void endFunction(InterpreterInfo info) {
        if (thisInfo != null) {
            info.variables.remove(info.functionStackSize + "#" + thisInfo.name);
        }
        for (ArgumentInfo arg : argumentsInfo)
            info.variables.remove(info.functionStackSize + "#" + arg.name);
    }



}
