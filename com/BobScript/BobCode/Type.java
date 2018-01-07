package com.BobScript.BobCode;

/**
 * Created by zinov on 20.02.2016.
 */
public enum Type {
    NULL, INT, FLOAT, STRING, ARRAY, SET, MAP, BOOLEAN, FUNCTION;


    @Override
    public String toString() {
        switch (this) {
            case INT:
                return "integer";
            case NULL:
                return "null";
            case FLOAT:
                return "float";
            case STRING:
                return "string";
            case ARRAY:
                return "array";
            case SET:
                return "set";
            case MAP:
                return "set";
            case BOOLEAN:
                return "boolean";
            case FUNCTION:
                return "function";
            default:
                return "unknown";
        }
    }

    public boolean isCopy() {
        if (this == INT || this == NULL || this == FLOAT)
            return true;
        return false;
    }
}
