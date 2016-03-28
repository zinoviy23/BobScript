package com.BobScript.Parsing;

/**
 * Created by zinov on 08.03.2016.
 */
public class Mark {
    public enum MarkValue {
        IF, WHILE, FUNCTION;

        @Override
        public String toString() {
            switch (this) {
                case IF:
                    return "if";
                case WHILE:
                    return "while";
                case FUNCTION:
                    return "function";
            }

            return "";
        }

        public static MarkValue getByInt(int value) {
            switch (value) {
                case 0:
                    return IF;
                case 1:
                    return WHILE;
                case 2:
                    return FUNCTION;
            }

            return IF;
        }

    }

    private MarkValue mark;
    private int position;

    public Mark(MarkValue mark, int position) {
        this.mark = mark;
        this.position = position;
    }

    public MarkValue getMark() { return mark; }
    public int getPosition() { return position; }
}
