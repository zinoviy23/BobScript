package com.BobScript.BobCode;

/**
 * Created by zinov on 20.02.2016.
 * Enum for commands
 */
public enum Commands {

    /**
     * Exit command
     */
    EXIT,
    /**
     * Push command, push element in stack <br>
     *     push type data
     */
    PUSH,
    /**
     * Assign first value with second
     */
    ASSIGN,
    /**
     * Delete variable
     */
    DELETE,
    /**
     * Add first and second values and push result at stack
     */
    ADD,
    /**
     * First value - second value and result at stack
     */
    SUB,
    LESSER,
    GREATER,
    EQUAL,
    CONDITION,
    END_CONDITION, GOTO, MULT,
    FUNCTION, ARG_COUNT, ARGUMENT, CALL, END_FUNCTION, ASSIGN_ADD, CREATE_ARRAY, CREATE_DIMENSIONAL_ARRAY,
    CREATE_OR_PUSH, GET_FROM, RETURN, PARSE_BREAK, PARSE_CONTINUE, UNARY_MINUS, INCREMENT, GET_FIELD_FROM,
    CALL_FROM, DIV, MOD, RANGE, ADD_METHOD, CLASS, END_CLASS, FIELD, CREATE_INSTANCE;

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
                return "parse_continue";
            case INCREMENT:
                return "increment";
            case UNARY_MINUS:
                return "unary_minus";
            case GET_FIELD_FROM:
                return "get_field_from";
            case CALL_FROM:
                return "call_from";
            case DIV:
                return "div";
            case MOD:
                return "mod";
            case RANGE:
                return "range";
            case ADD_METHOD:
                return "add_method";
            case CLASS:
                return "class";
            case FIELD:
                return "field";
            case END_CLASS:
                return "end_class";
            case CREATE_INSTANCE:
                return "create_instance";
        }

        return "";
    }
}
