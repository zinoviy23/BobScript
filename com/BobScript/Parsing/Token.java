package com.BobScript.Parsing;

/**
 * Created by zinov on 21.02.2016.
 */
public class Token {
    public enum TokenTypes {
        DELIMITER, NONE, NUMBER, CONST_STRING, KEYWORD
    }

    public Token(String token, TokenTypes type, int priority) {
        this.token = token;
        this.priority = priority;
        this.type = type;
    }

    public Token() {
        this.token = "";
        this.priority = 0;
        this.type = TokenTypes.NONE;
    }

    String getToken() { return token; }
    int getPriority() { return priority; }
    TokenTypes getType() { return type; }


    private String token;
    private TokenTypes type;
    private int priority;
}
