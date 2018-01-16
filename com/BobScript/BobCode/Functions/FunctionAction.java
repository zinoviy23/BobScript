package com.BobScript.BobCode.Functions;

import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;

import java.util.Stack;

/**
 * Abstract class for all functions
 */
public abstract class FunctionAction {
    /**
     * boolean value that says is function builtin or users
     */
    protected boolean isBuiltin = true;

    /**
     * Count argumnets of function
     * @return integer numbers, count of arguments
     */
    public abstract int getArgumentsCount();

    /**
     * Function action
     * @param info interpreter info
     */
    public abstract void Action(InterpreterInfo info);

    /**
     * Is function builtin
     * @return true if yes, is it
     */
    public boolean isBuiltinFunction() {return isBuiltin; }

}
