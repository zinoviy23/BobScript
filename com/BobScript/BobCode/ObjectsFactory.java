package com.BobScript.BobCode;

import com.BobScript.BobCode.Functions.BuiltinMethods.Array.AddMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.Array.ConvertMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.Array.ForEachMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.Function.CallMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.String.LengthMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.String.SplitMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.String.SubstringMethodAction;
import com.BobScript.BobCode.Functions.FunctionAction;

import java.util.ArrayList;
import java.util.HashMap;

public class ObjectsFactory {

    public static StackData createString(String value) {
        StackData obj = new StackData(value, Type.STRING);
        HashMap<String, FunctionAction> methods = new HashMap<>();
        methods.put("length", new LengthMethodAction(obj));
        methods.put("split", new SplitMethodAction(obj));
        methods.put("substring", new SubstringMethodAction(obj));
        obj.setMethods(methods);
        return obj;
    }

    public static StackData createArray(InterpreterInfo info, int arraySize) {
        ArrayList<StackData> array = new ArrayList<>();

        for (int i = 0; i < arraySize; i++)
            array.add(info.stack.pop().clone());

        StackData arrayObj = new StackData(array, Type.ARRAY);

        HashMap<String, FunctionAction> methods = new HashMap<>();
        methods.put("length", new com.BobScript.BobCode.Functions.BuiltinMethods.Array.LengthMetodAction(arrayObj));
        methods.put("add", new AddMethodAction(arrayObj));
        methods.put("convert", new ConvertMethodAction(arrayObj));
        methods.put("foreach", new ForEachMethodAction(arrayObj));
        arrayObj.setMethods(methods);

        return arrayObj;
    }

    public static StackData createArray(ArrayList<StackData> data) {
        StackData arrayObj = new StackData(data, Type.ARRAY);

        HashMap<String, FunctionAction> methods = new HashMap<>();
        methods.put("length", new com.BobScript.BobCode.Functions.BuiltinMethods.Array.LengthMetodAction(arrayObj));
        methods.put("add", new AddMethodAction(arrayObj));
        methods.put("convert", new ConvertMethodAction(arrayObj));
        methods.put("foreach", new ForEachMethodAction(arrayObj));
        arrayObj.setMethods(methods);

        return arrayObj;
    }

    public static StackData createFunction(FunctionAction function) {
        StackData funcObj = new StackData(function, Type.FUNCTION);

        HashMap<String, FunctionAction> methods = new HashMap<>();
        methods.put("call", new CallMethodAction(funcObj));
        funcObj.setMethods(methods);

        return funcObj;
    }
}
