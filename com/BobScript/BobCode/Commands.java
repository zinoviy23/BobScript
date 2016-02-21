package com.BobScript.BobCode;

/**
 * Created by zinov on 20.02.2016.
 */
public enum Commands {
    EXIT, PUSH, CREATE, DELETE, ASSIGN, ADD, SUB;

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
        }

        return "";
    }
}
