package com.BobScript.BobCode;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.Types.TypeInfo;

import java.util.ArrayList;

/**
 * Фабрика объектов
 */
public class ObjectsFactory {

    public static StackData createString(String value) {
        return new StackData(value, Type.STRING, TypeInfo.stringTypeInfo);
    }

    public static StackData createArray(InterpreterInfo info, int arraySize) {
        ArrayList<StackData> array = new ArrayList<>();

        for (int i = 0; i < arraySize; i++)
            array.add(info.stack.pop().clone());

        return new StackData(array, Type.ARRAY, TypeInfo.arrayTypeInfo);
    }

    public static StackData createArray(ArrayList<StackData> data) {
        return new StackData(data, Type.ARRAY, TypeInfo.arrayTypeInfo);
    }

    public static StackData createFunction(FunctionAction function) {
        return new StackData(function, Type.FUNCTION);
    }
}
