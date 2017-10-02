package com.BobScript.BobCode;

import com.BobScript.BobCode.Functions.FunctionAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class InterpreterInfo {
    public Stack<StackData> stack;
    public Map<String, Variable> variables;
    public int commandIndex;
    public HashMap<String, FunctionAction> functions;
    public Stack<FunctionAction> functionStack;

    public InterpreterInfo() {
        commandIndex = 0;
        stack = new Stack<>();
        variables = new HashMap<>();
        functions = new HashMap<>();
        functionStack = new Stack<>();
    }
}
