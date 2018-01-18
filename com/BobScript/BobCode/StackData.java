package com.BobScript.BobCode;

import com.BobScript.BobCode.Functions.FunctionAction;
import com.BobScript.BobCode.Types.TypeInfo;

import java.io.BufferedReader;
import java.util.*;

/**
 * Created by zinov on 20.02.2016.
 *
 * class for elements of stack
 * type and data
 */
public class StackData {
    protected Object data;
    protected Type type;
    private TypeInfo typeInfo;

    private HashMap<String, StackData> fields = new HashMap<>();
    private HashMap<String, FunctionAction> methods = new HashMap<>();

    /**
     * Возвращает поле объекта
     * @param name
     * @return
     */
    public StackData getField(String name) {
        return (!fields.containsKey(name)) ? null : fields.get(name);
    }

    /**
     * Возвращает метод объекта
     * @param name
     * @return
     */
    public  FunctionAction getMethod(String name) {
        if (methods.containsKey(name))
            return methods.get(name);
        return typeInfo.getMethod(name);
    }

    public void setFields(HashMap<String, StackData> fields) {
        this.fields = fields;
    }

    public void setMethods(HashMap<String, FunctionAction> methods) {
        this.methods = methods;
    }

    public StackData(Object data, Type type) {
        this.data = data;
        this.type = type;
    }

    public void assignCopy(StackData sd) {
        StackData newValue = sd.clone();
        data = newValue.getData();
        type = newValue.getType();
        typeInfo = newValue.typeInfo;
        methods = sd.methods;
    }

    public void assignPointer(StackData sd) {
        data = sd.getData();
        type = sd.getType();
    }

    public void addMethod(String name, FunctionAction action) {
        methods.put(name, action);
    }

    public StackData() {
        this.data = null;
        this.type = Type.NULL;
    }

    public StackData(Object data, Type type, TypeInfo typeInfo) {
        this.data = data;
        this.type = type;
        this.typeInfo = typeInfo;
    }

    public Object getData() { return data; }

    public Type getType() { return type; }


    public static String cloneString(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
            stringBuilder.append(str.charAt(i));

        return new String(stringBuilder);
    }

    public static String setToString(Set <Variable> set) {
        String str = "{";
        Iterator <Variable> it = set.iterator();
        while (it.hasNext()) {
            str += it.next().toString();
            if (it.hasNext())
                str += ", ";
        }
        str += "}";
        return str;
    }

    public static String mapToString(Map <Variable, Variable> map) {
        String str = "{";
        Iterator <Map.Entry<Variable, Variable>> it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Variable, Variable> now = it.next();
            str += now.getKey().toString() + ": " + now.getValue().toString();
            if (it.hasNext())
                str += ", ";
        }

        str += "}";
        return str;
    }

    @Override
    public StackData clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException ex) { }
        StackData newStackData;
        switch (type) {
            case INT:
            case FLOAT:
                newStackData =  new StackData(data, type);
                break;
            case STRING:
                newStackData = ObjectsFactory.createString(cloneString((String)data));
                break;
            case ARRAY:
                newStackData =  ObjectsFactory.createArray((ArrayList<StackData>)((ArrayList) data).clone());
                break;
            case FUNCTION:
                newStackData = ObjectsFactory.createFunction((FunctionAction) data);
                break;
            case FILE:
                newStackData = ObjectsFactory.createFile((BufferedReader)data, (String)fields.get("name").getData());
                break;
            default:
                newStackData = new  StackData(data, type);
                break;
        }
        return newStackData;
    }

    private String methodsToString() {
        StringBuilder stringBuilder = new StringBuilder("Methods {");
        for (Map.Entry<String, FunctionAction> el : methods.entrySet()) {
            stringBuilder.append(el.getKey()).append("(").append(el.getValue().getArgumentsCount()).append(") ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        if (data != null) return data.toString() + "->" + type.toString() + ", " + typeInfo + ",\n" + methodsToString();
        else return "nothing->" + type.toString();
    }
}
