package com.BobScript.BobCode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;

/**
 * Created by zinov on 20.02.2016.
 */


public class StackData {
    protected Object data;
    protected Type type;

    public StackData(Object data, Type type) {
        this.data = data;
        this.type = type;
    }

    public StackData() {
        this.data = null;
        this.type = Type.NULL;
    }

    Object getData() { return data; }
    Type getType() { return type; }


    public static String cloneString(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
            stringBuilder.append(str.charAt(i));

        return new String(stringBuilder);
    }

    public static String setToString(Set <Varible> set) {
        String str = "{";
        Iterator <Varible> it = set.iterator();
        while (it.hasNext()) {
            str += it.next().toString();
            if (it.hasNext())
                str += ", ";
        }
        str += "}";
        return str;
    }

    public static String mapToString(Map <Varible, Varible> map) {
        String str = "{";
        Iterator <Map.Entry<Varible, Varible>> it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Varible, Varible> now = it.next();
            str += now.getKey().toString() + ": " + now.getValue().toString();
            if (it.hasNext())
                str += ", ";
        }

        str += "}";
        return str;
    }

    @Override
    public StackData clone() {
        switch (type) {
            case INT:
            case DOUBLE:
                return new StackData(data, type);

            case STRING:
                return new StackData(cloneString((String)data), type);
            case ARRAY:
                return new StackData(((ArrayList<Varible>)data).clone(), type);
            default:
                return new StackData(data, type);
        }
    }

    @Override
    public String toString() {
        if (data != null) return data.toString() + "->" + type.toString();
        else return "nothing->" + type.toString();
    }
}
