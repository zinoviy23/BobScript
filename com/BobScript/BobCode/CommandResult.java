package com.BobScript.BobCode;

/**
 * Created by Alexandr on 19.09.2017.
 * Command results enum
 */
public enum CommandResult {
    /**
     * Commands success
     */
    OK,
    /**
     * Commands success and interpreter mustn't increment current command index
     */
    CONTINUE_OK,
    /**
     * Command throw error
     */
    ERROR
}
