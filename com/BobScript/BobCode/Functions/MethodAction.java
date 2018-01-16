package com.BobScript.BobCode.Functions;

import com.BobScript.BobCode.StackData;


public abstract class MethodAction extends FunctionAction {
    protected StackData objectPointer;

    public MethodAction(StackData obj) {
        objectPointer = obj;
    }


}
