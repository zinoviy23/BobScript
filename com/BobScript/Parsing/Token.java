package com.BobScript.Parsing;

/**
 * Created by zinov on 21.02.2016.
 */
public class Token {
    public enum TokenTypes {
        DELIMITER, NONE, NUMBER, CONST_STRING, KEYWORD, FOR_PARSING;

        @Override
        public String toString() {
            switch (this) {
                case DELIMITER:
                    return "delimiter";
                case NONE:
                    return "none";
                case NUMBER:
                    return "number";
                case CONST_STRING:
                    return "const string";
                case KEYWORD:
                    return "keyword";
                case FOR_PARSING:
                    return "for parsing";
                default:
                    return "";
            }
        }
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

    public String getToken() { return token; }
    public int getPriority() { return priority; }
    public TokenTypes getType() { return type; }


    public boolean isDelimiter() { return type == TokenTypes.DELIMITER; }
    public boolean isNumber() { return type == TokenTypes.NUMBER; }
    public boolean isConstString() { return type == TokenTypes.CONST_STRING; }
    public boolean isKeyword() { return type == TokenTypes.KEYWORD; }
    public boolean isOpenParenthesis() { return token.equals("("); }
    public boolean isCloseParenthesis() { return token.equals(")"); }
    public boolean isForParsing() { return type == TokenTypes.FOR_PARSING; }
    public void setUsed() {priority = 0;}

    private String token;
    private TokenTypes type;
    private int priority;

    @Override
    public String toString() {
        return "[" + token + ", " + type.toString() + ", " + priority + "]";
    }

    public Token copy() {
        return new Token(token, type, priority);
    }
}
