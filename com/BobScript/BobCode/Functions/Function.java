package com.BobScript.BobCode.Functions;

/**
 * Created by zinov on 08.03.2016.
 */
public class Function {
    public int returnPosition;
    public FunctionAction action;
    public String name;


    public Function(int returnPosition, FunctionAction action, String name) {
        this.returnPosition = returnPosition;
        this.action = action;
        this.name = name;
    }
}
