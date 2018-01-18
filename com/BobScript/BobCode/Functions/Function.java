package com.BobScript.BobCode.Functions;

import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;

import java.util.ArrayList;

/**
 * Created by zinov on 08.03.2016. <br>
 * Information about current called function
 */
public class Function {
    /**
     * command position that must be after the function stops
     */
    public int returnPosition;
    /**
     * function body
     */
    public FunctionAction action;
    /**
     * function name
     */
    public String name;
    private int stackIndex;
    private ArrayList<String> functionsVariables;

    /**
     * Gets function variable
     * @param name variable name
     * @param info interpreter information
     * @return null if there isn't variable with this name
     */
    public StackData getVariable(String name, InterpreterInfo info) {
        int parentStackIndex = ((UsersFunctionAction)action).parentFunctionStackSize - 1;
        //System.out.println(stackIndex + " " + name + " kek");
        if (info.variables.containsKey((stackIndex + 1) + "#" + name)) {
            return info.variables.get((stackIndex + 1) + "#" + name);
        } else if (parentStackIndex >= 0) {
            //System.out.println("Loool " + info.functionStack.get(parentStackIndex));
            return info.functionStack.get(parentStackIndex).getVariable(name, info);
        }
        return null;
    }

    /**
     * Constructor of Function class
     * @param returnPosition index of return position in program
     * @param action function that called
     * @param name name of function
     * @param stackIndex index in stack
     */
    public Function(int returnPosition, FunctionAction action, String name, int stackIndex) {
        this.returnPosition = returnPosition;
        this.action = action;
        this.name = name;
        this.stackIndex = stackIndex;
        this.functionsVariables = new ArrayList<>();
    }

    /**
     * Overrided toString
     * @return name and action
     */
    @Override
    public String toString() {
        return name + " " + action;
    }

    /**
     * Adds variable name in current function instance
     * @param name name of variable
     */
    public void addVariable(String name) {
        functionsVariables.add(name);
    }

    /**
     * Deletes variables, that created in this function
     * @param info interpreter info
     */
    public void deleteVariables(InterpreterInfo info) {
        for (String name : functionsVariables) {
            info.variables.remove(name);
        }
    }
}
