package com.BobScript.BobCode;

import com.BobScript.BobCode.Functions.FunctionAction;

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
        return (!methods.containsKey(name)) ? null : methods.get(name);
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
        setMethods(newValue.methods);
        setFields(newValue.fields);
    }

    public void assignPointer(StackData sd) {
        data = sd.getData();
        type = sd.getType();
    }

    public StackData() {
        this.data = null;
        this.type = Type.NULL;
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
            switch (type) {
                case INT:
                case FLOAT:
                    return new StackData(data, type);
                case STRING:
                    return ObjectsFactory.createString(cloneString((String)data));
                case ARRAY:
                    return ObjectsFactory.createArray((ArrayList<StackData>)((ArrayList) data).clone());
                case FUNCTION:
                    return ObjectsFactory.createFunction((FunctionAction) data);
                default:
                    return new StackData(data, type);
            }
        } catch (CloneNotSupportedException ex) {
            switch (type) {
                case INT:
                case FLOAT:
                    return new StackData(data, type);
                case STRING:
                    return ObjectsFactory.createString(cloneString((String)data));
                case ARRAY:
                    return ObjectsFactory.createArray((ArrayList<StackData>)((ArrayList) data).clone());
                case FUNCTION:
                    return ObjectsFactory.createFunction((FunctionAction) data);
                default:
                    return new StackData(data, type);
            }
        }
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
        if (data != null) return data.toString() + "->" + type.toString() + ", " + methodsToString();
        else return "nothing->" + type.toString();
    }
}
