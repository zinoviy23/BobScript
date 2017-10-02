package com.BobScript.BobCode.Functions;

import com.BobScript.BobCode.Variable;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zinov on 08.03.2016.
 */
public class Function {
    private int startPosition, returnPosition;
    private ArrayList<String> arguments;
    private int stackNumber;

    public Function(int startPosition, int returnPosition, ArrayList<String> arguments, int stackNumber) {
        this.startPosition = startPosition;
        this.returnPosition = returnPosition;
        this.arguments = arguments;
        this.stackNumber = stackNumber;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getReturnPosition() {
        return returnPosition;
    }

    public ArrayList<String> getArguments() {
        return arguments;
    }

    public int getStackNumber() {
        return stackNumber;
    }
}
