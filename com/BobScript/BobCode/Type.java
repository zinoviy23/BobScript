package com.BobScript.BobCode;

/**
 * Created by zinov on 20.02.2016.
 */
public enum Type {
    NULL, INT, FLOAT, STRING, ARRAY, SET, MAP, BOOLEAN, FUNCTION, FILE, RANGE, USERS;

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
            case FILE:
                return "file";
            case RANGE:
                return "range";
            case USERS:
                return "users";
            default:
                return "unknown";
        }
    }

    public boolean isCopy() {
        return this == INT || this == NULL || this == FLOAT;
    }
}
