package com.BobScript.BobCode.Functions;

import com.BobScript.BobCode.InterpreterInfo;
import com.BobScript.BobCode.StackData;

import java.util.Stack;

public abstract class FunctionAction {
    public abstract int getArgumentsCount();
    public abstract void Action(InterpreterInfo info);
}
