package com.BobScript.BobCode;

/**
 * Created by zinov on 20.02.2016.
 */
public enum Commands {
    EXIT, PUSH, CREATE, DELETE, ASSIGN, ADD, SUB, LESSER, GREATER, CONDITION, GOTO, OUTPUT, MULT;

    public static Commands toCommands (int val) {
        switch (val) {
            case 0:
                return EXIT;
            case 1:
                return PUSH;
            case 2:
                return CREATE;
            case 3:
                return DELETE;
            case 4:
                return ASSIGN;
            case 5:
                return ADD;
            case 6:
                return SUB;
            case 7:
                return LESSER;
            case 8:
                return GREATER;
            case 9:
                return CONDITION;
            case 10:
                return GOTO;
            case 11:
                return MULT;
        }

        return EXIT;
    }

    @Override
    public String toString() {
        switch (this) {
            case EXIT:
                return "exit";
            case PUSH:
                return "push";
            case CREATE:
                return "create";
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
            case GOTO:
                return "goto";
            case OUTPUT:
                return "output";
            case MULT:
                return "mult";
        }

        return "";
    }
}
