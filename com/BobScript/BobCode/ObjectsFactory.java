package com.BobScript.BobCode;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.Types.TypeInfo;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;

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

    public static StackData createFile(BufferedReader reader, String name) {
        StackData obj = new StackData(reader, Type.FILE, TypeInfo.fileTypeInfo);
        StackData nameField = createString(name);
        HashMap<String, StackData> fields = new HashMap<>();
        fields.put("name", nameField);
        obj.setFields(fields);
        return obj;
    }
}
