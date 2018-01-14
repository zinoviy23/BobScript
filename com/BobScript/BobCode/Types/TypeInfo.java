package com.BobScript.BobCode.Types;

import com.BobScript.BobCode.Functions.BuiltinMethods.Array.AddMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.Array.CollectMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.Array.ConvertMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.Array.ForEachMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.File.ReadLineMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.String.JoinMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.String.LengthMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.String.SplitMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.String.SubstringMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.ToStrMethodAction;
import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.StackData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс для представления информации для типа
 */
public class TypeInfo {
    /**
     * методы класса
     */
    private HashMap<String, FunctionAction> methods = new HashMap<>();
    /**
     * Статические поля
     */
    private HashMap<String, StackData> staticFields = new HashMap<>();
    /**
     * поля объекта
     */
    private ArrayList<String> fields = new ArrayList<>();

    public static TypeInfo stringTypeInfo;
    public static TypeInfo arrayTypeInfo;
    public static TypeInfo fileTypeInfo;

    static {
        stringTypeInfo = new TypeInfo();
        HashMap<String, FunctionAction> methods = new HashMap<>();
        methods.put("length", new LengthMethodAction());
        methods.put("split", new SplitMethodAction());
        methods.put("substring", new SubstringMethodAction());
        methods.put("toStr", new ToStrMethodAction());
        methods.put("join", new JoinMethodAction());
        stringTypeInfo.methods = methods;

        arrayTypeInfo = new TypeInfo();
        methods = new HashMap<>();
        methods.put("length", new com.BobScript.BobCode.Functions.BuiltinMethods.Array.LengthMetodAction());
        methods.put("convert", new ConvertMethodAction());
        methods.put("foreach", new ForEachMethodAction());
        methods.put("add", new AddMethodAction());
        methods.put("collect", new CollectMethodAction());
        methods.put("toStr", new ToStrMethodAction());
        arrayTypeInfo.methods = methods;

        fileTypeInfo = new TypeInfo();
        methods = new HashMap<>();
        methods.put("readLine", new ReadLineMethodAction());
        ArrayList<String> fields = new ArrayList<>();
        fields.add("name");
        fileTypeInfo.fields = fields;
        fileTypeInfo.methods = methods;
    }

    public TypeInfo() {

    }

    public TypeInfo(HashMap<String, FunctionAction> methods, HashMap<String, StackData> staticFields, ArrayList<String> fields) {
        this.methods = methods;
        this.staticFields = staticFields;
        this.fields = fields;
    }

    public HashMap<String, FunctionAction> getMethods() {
        return methods;
    }

    public void setMethods(HashMap<String, FunctionAction> methods) {
        this.methods = methods;
    }

    public HashMap<String, StackData> getStaticFields() {
        return staticFields;
    }

    public void setStaticFields(HashMap<String, StackData> staticFields) {
        this.staticFields = staticFields;
    }

    public ArrayList<String> getFields() {
        return fields;
    }

    public void setFields(ArrayList<String> fields) {
        this.fields = fields;
    }

    public FunctionAction getMethod(String name) {
        return (!methods.containsKey(name)) ? null : methods.get(name);
    }
}
