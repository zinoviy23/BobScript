package com.BobScript.BobCode.Types;

import com.BobScript.BobCode.Functions.BuiltinMethods.Array.*;
import com.BobScript.BobCode.Functions.BuiltinMethods.File.ReadLineMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.Int.IsEvenMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.Int.IsOddMethodAction;
import com.BobScript.BobCode.Functions.BuiltinMethods.Int.IsZeroMethodAction;
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
 * Class for representation type
 */
public class TypeInfo {
    /**
     * types methods
     */
    private HashMap<String, FunctionAction> methods = new HashMap<>();
    /**
     * types fields
     */
    private HashMap<String, StackData> staticFields = new HashMap<>();
    /**
     * instance fields
     */
    private ArrayList<String> fields = new ArrayList<>();

    /**
     * Constructor of class
     */
    private FunctionAction constructor;

    /**
     * string typeInfo
     */
    public static TypeInfo stringTypeInfo;
    /**
     * array typeInfo
     */
    public static TypeInfo arrayTypeInfo;
    /**
     * File typeInfo
     */
    public static TypeInfo fileTypeInfo;
    /**
     * Range typeInfo
     */
    public static TypeInfo rangeTypeInfo;

    /**
     * Int typeInfo
     */
    public static TypeInfo intTypeInfo;


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
        arrayTypeInfo.constructor = new ArrayConstructorAction();

        fileTypeInfo = new TypeInfo();
        methods = new HashMap<>();
        methods.put("readLine", new ReadLineMethodAction());
        ArrayList<String> fields = new ArrayList<>();
        fields.add("name");
        fileTypeInfo.fields = fields;
        fileTypeInfo.methods = methods;

        rangeTypeInfo = new TypeInfo();
        methods = new HashMap<>();
        methods.put("foreach", new com.BobScript.BobCode.Functions.BuiltinMethods.Range.ForEachMethodAction());
        rangeTypeInfo.methods = methods;

        intTypeInfo = new TypeInfo();
        methods = new HashMap<>();
        methods.put("odd?", new IsOddMethodAction());
        methods.put("even?", new IsEvenMethodAction());
        methods.put("zero?", new IsZeroMethodAction());
        methods.put("toStr", new ToStrMethodAction());
        intTypeInfo.methods = methods;
    }

    /**
     * Simple constructor
     * extend type from Object
     */
    public TypeInfo() {
        methods.put("toStr", new ToStrMethodAction());
    }

    /**
     * Constructor with parameters
     * @param methods HashMap with methods
     * @param staticFields HashMap with static fields
     * @param fields ArrayList with fields names
     */
    public TypeInfo(HashMap<String, FunctionAction> methods, HashMap<String, StackData> staticFields, ArrayList<String> fields) {
        this.methods = methods;
        this.staticFields = staticFields;
        this.fields = fields;
    }

    /**
     * Gets methods
     * @return methods
     */
    public HashMap<String, FunctionAction> getMethods() {
        return methods;
    }

    /**
     * Sets methods
     * @param methods new methods value
     */
    public void setMethods(HashMap<String, FunctionAction> methods) {
        this.methods = methods;
    }

    /**
     * Gets static fields
     * @return static fields
     */
    public HashMap<String, StackData> getStaticFields() {
        return staticFields;
    }

    /**
     * Sets static fields
     * @param staticFields new static fields
     */
    public void setStaticFields(HashMap<String, StackData> staticFields) {
        this.staticFields = staticFields;
    }

    /**
     * Gets fields names
     * @return fields names
     */
    public ArrayList<String> getFields() {
        return fields;
    }

    /**
     * Sets fields names
     * @param fields new fields names
     */
    public void setFields(ArrayList<String> fields) {
        this.fields = fields;
    }

    /**
     * Gets method
     * @param name method name
     * @return null, if there isn't method with this name
     */
    public FunctionAction getMethod(String name) {
        return (!methods.containsKey(name)) ? null : methods.get(name);
    }

    /**
     * Add method to type
     * @param name Name of method
     * @param functionAction Function action
     */
    public void addFunction(String name, FunctionAction functionAction) {
        methods.put(name, functionAction);
    }

    /**
     * Add field to type
     * @param field field's name
     */
    public void addField(String field) {
        fields.add(field);
    }

    /**
     * Gets type constructor
     * @return current constructor
     */
    public FunctionAction getConstructor() {
        return constructor;
    }

    /**
     * Set type constructor
     * @param constructor new constructor
     */
    public void setConstructor(FunctionAction constructor) {
        this.constructor = constructor;
    }
}
