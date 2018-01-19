package com.BobScript.BobCode;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.Types.Range;
import com.BobScript.BobCode.Types.TypeInfo;
import com.sun.deploy.security.ValidationState;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static StackData createRange(long start, long end) {
        return new StackData(new Range(start, end), Type.RANGE, TypeInfo.rangeTypeInfo);
    }

    public static StackData createInt(long value) {
        return new StackData(value, Type.INT, TypeInfo.intTypeInfo);
    }

    public static StackData createObject(TypeInfo type, int arguments, InterpreterInfo info) {
        StackData data;
        if (type == TypeInfo.arrayTypeInfo)
            data = ObjectsFactory.createArray(info, 0);
        else
            data = new StackData(null, Type.USERS, type);
        HashMap<String, StackData> fields = new HashMap<>();
        for (String s : type.getFields()) {
            fields.put(s, new StackData(null, Type.NULL));
        }
        data.setFields(fields);

        FunctionAction constructor = type.getConstructor();

        if (constructor == null || constructor.getArgumentsCount() != arguments) {
            // TODO: nothing, if constructor == null
//            TODO: Error, if wrong arguments count
        } else {
            info.stack.push(data);
            info.interpreter.callFunction(constructor);
        }

        return data;
    }

    public static StackData cloneUsersObject(StackData obj) {
        StackData newObj = new StackData(null, Type.USERS, obj.getTypeInfo());
        newObj.setFields((HashMap<String, StackData>) obj.getFields().clone());

        return newObj;
    }
}
