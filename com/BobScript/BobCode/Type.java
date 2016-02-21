package com.BobScript.BobCode;

/**
 * Created by zinov on 20.02.2016.
 */
public enum Type {
    NULL, INT, DOUBLE, STRING, ARRAY, SET, MAP, BOOLEAN;


    @Override
    public String toString() {
        switch (this) {
            case INT:
                return "integer";
            case NULL:
                return "null";
            case DOUBLE:
                return "double";
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
            default:
                return "unknown";
        }
    }

    public boolean isCopy() {
        if (this == INT && this == NULL && this == DOUBLE)
            return true;
        return false;
    }
}
