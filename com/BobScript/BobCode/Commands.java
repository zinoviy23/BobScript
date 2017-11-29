package com.BobScript.BobCode;

/**
 * Created by zinov on 20.02.2016.
 */
public enum Commands {
    EXIT, PUSH, ASSIGN, DELETE, ADD, SUB, LESSER, GREATER, EQUAL, CONDITION, END_CONDITION, GOTO, MULT,
    FUNCTION, ARG_COUNT, ARGUMENT, CALL, END_FUNCTION, ASSIGN_ADD, CREATE_ARRAY, CREATE_DIMENSIONAL_ARRAY,
    CREATE_OR_PUSH, GET_FROM, RETURN, PARSE_BREAK, PARSE_CONTINUE;

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
            case ASSIGN_ADD:
                return "assign_add";
            case CREATE_ARRAY:
                return "create_array";
            case CREATE_OR_PUSH:
                return "create_or_push";
            case GET_FROM:
                return "get_from";
            case RETURN:
                return "return";
            case CREATE_DIMENSIONAL_ARRAY:
                return "create_dimensional_array";
            case PARSE_BREAK:
                return "parse_break";
            case PARSE_CONTINUE:
                return "paese_continue";
        }

        return "";
    }
}
