package com.BobScript.BobCode;

import com.BobScript.BobCode.StackData;

/**
 * Created by zinov on 20.02.2016.
 */
public class Variable extends StackData {

    public Variable() {
        super();
    }

    public void assignCopy(StackData sd) {
        StackData newValue = sd.clone();
        data = newValue.getData();
        type = newValue.getType();
    }

    public void assignPointer(StackData sd) {
        data = sd.getData();
        type = sd.getType();
    }

    @Override
    public StackData clone() {
        return new StackData(data, type).clone();
    }

}
