package com.BobScript.Parsing;

import com.sun.istack.internal.NotNull;

import java.util.*;

/**
 * Created by zinov on 23.02.2016.
 */
public class Operand {
    private ArrayList <Token> tokens;
    /**
     * Ключевые слова
     */
    public static String[] keywords = {"var", "if", "end", "while", "func",
            "return", "delete", "for", "array", "break", "continue",
    "else", "elif"};

    private static Set<String> keywordsSet;

    public Operand() {
        initKeywords();
        tokens = new ArrayList<>();
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    /** make array of tokens from string
     *
     * @param operand
     */
    public Operand(String operand) {
        initKeywords();
        tokens = new ArrayList<>();
        int parenthesisCounter = 1;
        Stack <Boolean> isFunctionParenthesis = new Stack<>();
        int pr = 0;
        for (int i = 0; i < operand.length(); i++) {
            if (operand.charAt(i) == '/' && i < operand.length() - 1 && operand.charAt(i + 1) == '/')
                break;
            if (isSpace(operand.charAt(i))) {
                continue;
            }
            else if (isLatter(operand.charAt(i))) {
                int ind = i + 1;
                while (ind < operand.length() && isLatter(operand.charAt(ind))) {
                    ind++;
                }
                String currentOperand = operand.substring(i, ind);
                addToken(currentOperand, getType(currentOperand),
                        getOperatorPriority(currentOperand, isLastDelimiter()) + pr);
                i = ind - 1;
            }
            else if (operand.charAt(i) == '(') {
                if (isLastDelimiter()) {
                    isFunctionParenthesis.push(false);
                    pr += 40;
                }
                else {
                    isFunctionParenthesis.push(true);
                    addToken("(", Token.TokenTypes.DELIMITER, pr + 20);
                }
            }
            else if (operand.charAt(i) == ')') {
                if (!isFunctionParenthesis.peek()) {
                    pr -= 40;
                    parenthesisCounter += 1;
                }
                else {
                    addToken(")", Token.TokenTypes.DELIMITER, pr);
                }
                isFunctionParenthesis.pop();
            }
            else if (operand.charAt(i) == '{') {
                addToken("{", Token.TokenTypes.DELIMITER, pr);
            }
            else if (operand.charAt(i) == '}') {
                addToken("}", Token.TokenTypes.DELIMITER, pr);
            }
            else if (operand.charAt(i) == '[') {
                addToken("[", Token.TokenTypes.DELIMITER, pr + 20);
            }
            else if (operand.charAt(i) == ']') {
                addToken("]", Token.TokenTypes.DELIMITER, pr);
            }
            else if (isDigit(operand.charAt(i))) {
                int ind = i + 1;
                while (ind < operand.length() && (isDigit(operand.charAt(ind)) || operand.charAt(ind) == '.')) {
                    ind++;
                }
                String currentOperand = operand.substring(i, ind);
                addToken(currentOperand, Token.TokenTypes.NUMBER,
                        getOperatorPriority(currentOperand, false) + pr);
                i = ind - 1;
            }
            else if (isDelimiter(operand.charAt(i))) {
                int ind = i + 1;
                while (ind < operand.length() && isDelimiter(operand.charAt(ind)) && !isSpace(operand.charAt(ind))) {
                    ind++;
                }
                String currentOperand = operand.substring(i, ind);
                //if (currentOperand.equals(","))
                    //addToken(currentOperand, Token.TokenTypes.DELIMITER, 0);
                //else
                    addToken(currentOperand, Token.TokenTypes.DELIMITER,
                            getOperatorPriority(currentOperand, isLastDelimiter()) + pr);
                i = ind - 1;
            }
            else if (operand.charAt(i) == '\'')  // if " go to next " and this str is Const String
            {
                int ind = i + 1;
                while (ind < operand.length() && operand.charAt(ind) != '\'') {
                    ind++;
                }
                String currentOperand = operand.substring(i, ind + 1);
                addToken(currentOperand, Token.TokenTypes.CONST_STRING,
                        getOperatorPriority(currentOperand, false) + pr);
                i = ind;
            }
        }
    }

    public Operand(@NotNull Token[] tokens) {
        this.tokens = new ArrayList<>();
        this.tokens.addAll(Arrays.asList(tokens));
    }

    public void addToken(@NotNull Token tk) {
        tokens.add(tk);
    }

    public void addToken(String tk, Token.TokenTypes type, int priority) {
        addToken(new Token(tk, type, priority));
    }

    public Token get(int i) {
        return tokens.get(i);
    }

    public void pop(int i) { tokens.remove(i); }

    public int size() { return tokens.size(); }

    public Operand extractFrom(int start, int end) {
        Operand newOp = new Operand();
        for (int i = start; i <= end; i++)
        {
            newOp.addToken(tokens.get(i).copy());
            tokens.get(i).setUsed();
        }

        return newOp;
    }

    public Operand extractFromParenthesis(int open, int close) {
        if (!get(open).isOpenParenthesis() || !get(close).isCloseParenthesis())
            return new Operand();
        //tokens.remove(open);
        //tokens.remove(close - 1);
        return extractFrom(open + 1, close - 1);
    }

    public Operand[] split(String del) {
        ArrayList<Operand> ret = new ArrayList<>();
        int start = 0;
        int end = -1;
        for (int i = 0; i < tokens.size(); i++)
            if (tokens.get(i).getToken().equals(del)) {
                end = i - 1;
                ret.add(this.extractFrom(start, end));
                start = i + 1;
            }

        if (start < tokens.size()) {
            ret.add(this.extractFrom(start, tokens.size() - 1));
        }

        return ret.toArray(new Operand[ret.size()]);
    }

    public void remove(int i) {
        tokens.remove(i);
    }

    public void removeAll(int start, int end) {
        for (int i = start; i < end; i++)
            remove(start);
    }

    public void set(int i, Token t) {
        tokens.set(i, t);
    }

    // return index of max priority token
    public int next() {
        int index = -1;
        int max = -1;

        for (int i = 0; i < tokens.size(); i++) {
            if (max < tokens.get(i).getPriority()) {
                max = tokens.get(i).getPriority();
                index = i;
            }
        }

        return index;
    }

    /** return index of Close Parenthesis for Open Parenthesis,
     if argument index not Open Parenthesis return -1,
     if no Close Parenthesis return -2
      */
    public int getCloseParenthesis(int openParenthesis) {
        if (!get(openParenthesis).isOpenParenthesis())
            return -1;

        int balance = 0;
        for (int i = openParenthesis; i < size(); i++) {
            if (get(i).isOpenParenthesis())
                balance++;
            if (get(i).isCloseParenthesis())
                balance--;
            if (balance == 0)
                return i;
        }

        return -2;
    }

    public int getCloseBoxParenthesis(int openParenthesis) {
        if (!get(openParenthesis).getToken().equals("["))
            return -1;

        int balance = 0;
        for (int i = openParenthesis; i < size(); i++) {
            if (get(i).getToken().equals("["))
                balance++;
            if (get(i).getToken().equals("]"))
                balance--;
            if (balance == 0)
                return i;
        }
        System.out.println(balance);
        return -2;
    }

    public boolean isLastDelimiter() {
        return tokens.size() < 1 || tokens.get(tokens.size() - 1).isDelimiter() || tokens.get(tokens.size() - 1).isKeyword();
    }

    @Override
    public String toString() {
        String answ = "Tokens: {\n";
        for (Token tk : tokens) {
            answ += tk.toString() + "\n";
        }
        answ += "}";
        return answ;
    }

    // for Operand(String)
    public static boolean isDelimiter(char c) {
        switch (c) {
            case ' ':
            case ',':
            case '+':
            case '-':
            case '.':
            case '=':
            case '*':
            case '<':
            case '>':
            case ':':
            case ';':
                return true;
        }
        return false;
    }

    public static boolean isDelimiter(String s) {
        if (s.length() == 1)
            return isDelimiter(s.charAt(0));
        return s.equals("<=") || s.equals(">=") || s.equals("==") || s.equals("+=");
    }

    public static boolean isKeyword(String s) {
        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i].equals(s))
                return true;
        }

        return false;
    }

    public static boolean isSpace(char c) { return c == ' ' || c == '\t'; }

    public static boolean isLatter(char c) { return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '_'); }

    public static boolean isDigit(char c) { return c >= '0' && c <= '9'; }

    public static int getOperatorPriority(String s, boolean unary) {
        if (!unary) {
            if (s.equals("+") || s.equals("-"))
                return 14;

            if (s.equals("*"))
                return 15;

            if (s.equals("."))
                return 30;
            if (s.equals(":"))
                return 31;

            if (s.equals("<") || s.equals(">") || s.equals("=="))
                return 4;

            if (s.equals("=") || s.equals("+="))
                return 3;

            if (s.equals(",") || s.equals(";"))
                return 2;

            if (isKeyword(s))
                return 1000;
        }
        else {
            if (s.equals("-"))
                return 20;

            if (s.equals("++"))
                return 20;
        }
        return 1;
    }

    public static Token.TokenTypes getType(String s) {
        if (keywordsSet.contains(s))
            return Token.TokenTypes.KEYWORD;
        return Token.TokenTypes.NONE;
    }

    private static void initKeywords() {
        keywordsSet = new HashSet<>();
        for (int i = 0; i < keywords.length; i++) {
            keywordsSet.add(keywords[i]);
        }
    }
}
