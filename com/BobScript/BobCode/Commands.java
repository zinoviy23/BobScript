package com.BobScript.BobCode;

/**
 * Created by zinov on 20.02.2016.
 */
public enum Commands {
    EXIT, PUSH, ASSIGN, DELETE, ADD, SUB, LESSER, GREATER, EQUAL, CONDITION, END_CONDITION, GOTO, MULT,
        FUNCTION, ARG_COUNT, ARGUMENT, CALL, END_FUNCTION;

    @Override
    public String toString() {
        switch (this) {
            case EXIT:
                return "exit";
            case PUSH:
                return "push";
            case DELETE:
                return "delete";
            case ASSIGN:
                return "assign";
            case ADD:
                return "add";
            case SUB:
                return "sub";
            case LESSER:
                return "lesser";
            case GREATER:
                return "greater";
            case CONDITION:
                return "condition";
            case END_CONDITION:
                return "end_condition";
            case GOTO:
                return "goto";
            case MULT:
                return "multiply";
            case EQUAL:
                return "equal";
            case FUNCTION:
                return "function";
            case ARG_COUNT:
                return "arguments_count";
            case ARGUMENT:
                return "argument";
            case END_FUNCTION:
                return "end_function";
            case CALL:
                return "call";
        }

        return "";
    }
}
