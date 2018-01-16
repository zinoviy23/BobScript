package com.BobScript.BobCode.Functions;

import com.BobScript.BobCode.StackData;

/**
 * Class for methods. It used for function pointers
 */
public abstract class MethodAction extends FunctionAction {
    /**
     * Object, that has this method
     */
    protected StackData objectPointer;

    /**
     * Constructor
     * @param obj object with method
     */
    public MethodAction(StackData obj) {
        objectPointer = obj;
    }


}
