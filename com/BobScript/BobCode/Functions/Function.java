package com.BobScript.BobCode.Functions;

/**
 * Created by zinov on 08.03.2016.
 */
public class Function {
    public int returnPosition;
    public FunctionAction action;

    public Function(int returnPosition, FunctionAction action) {
        this.returnPosition = returnPosition;
        this.action = action;
    }
}
