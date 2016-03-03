package com.BobScript.BobCode;

import java.util.HashMap;
import java.util.Stack;
import java.util.Map;

/**
 * Created by zinov on 20.02.2016.
 *
 *  execute class, that run compiled code
 */
public class Interpriter {
    private Stack<StackData> stack;
    private Map<String, Varible> varibles;

    public Interpriter() {
        stack = new Stack<>();
        varibles = new HashMap<>();
    }


    public static boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    public static boolean tryInt(String s) {
        for (int i = 0; i < s.length(); i++)
            if (!isDigit(s.charAt(i)))
                return false;

        return true;
    }

    public static boolean tryDouble(String s) {
        boolean point = false;
        for (int i = 0; i < s.length(); i++) {
            if (!isDigit(s.charAt(i)))
                if (s.charAt(i) != '.')
                    return false;
                else if (i == s.length() - 1)
                    return false;
                else if (point)
                    return false;
                else {
                    point = true;
                }
        }

        return true;
    }

    public static boolean tryConstString(String s) {
        return s.length() > 1 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"';
    }

    public static boolean tryBoolean(String s) {
        return s.equals("true") || s.equals("false");
    }

    public static boolean tryNull(String s) { return s.equals("null"); }

    public static String toConstString(String s) {
        return s.substring(1, s.length() - 1);
    }

    // run commands
    public int execute(Command[] program) {
        for (Command c : program) {
            System.out.println(c);
        }
        int commandIndex = 0;
        while (commandIndex < program.length) {
            Command currentCommand = program[commandIndex];
            switch (currentCommand.getCommand()) {
                case EXIT: {
                    if (!currentCommand.getArgs()[0].equals("0")) {
                        Log.printError("FAILED: return code - " + currentCommand.getArgs()[0]);
                        return -100;
                    }
                    return 0;
                }

                case PUSH: {
                    String[] args = currentCommand.getArgs();

                    if (tryInt(args[0])) {
                        stack.add(new StackData(Long.parseLong(args[0]), Type.INT));
                    } else if (tryDouble(args[0])) {
                        stack.add(new StackData(Double.parseDouble(args[0]), Type.DOUBLE));
                    } else if (tryConstString(args[0])) {
                        stack.add(new StackData(toConstString(args[0]), Type.STRING));
                    } else if (tryBoolean(args[0])) {
                        stack.add(new StackData(Boolean.parseBoolean(args[0]), Type.BOOLEAN));
                    } else if (tryNull(args[0])) {
                        stack.add(new StackData(null, Type.NULL));
                    } else if (varibles.containsKey(args[0])) {
                        stack.add(varibles.get(args[0]).clone());
                    } else {
                        Log.printError("ERROR: " + currentCommand + "unknown type");
                    }
                }
                break;

                case CREATE: {
                    String[] args = currentCommand.getArgs();

                    if (varibles.containsKey(args)) {
                        Log.printError("ERROR: " + currentCommand + "this varible has been created");
                        return -1;
                    }

                    varibles.put(args[0], new Varible());
                }
                break;

                case ASSIGN: {
                    String[] args = currentCommand.getArgs();

                    if (stack.size() < 1) {
                        Log.printError("ERROR: " + currentCommand + "stack size too small");
                        return -2;
                    }

                    if (!varibles.containsKey(args[0])) {
                        Log.printError("ERROR" + currentCommand + "unknown name");
                        return -1;
                    }

                    StackData sd = stack.pop();
                    varibles.get(args[0]).assignCopy(sd);   // assign to copy for simple types
                }
                break;

                case DELETE: {
                    String[] args = currentCommand.getArgs();

                    if (!varibles.containsKey(args[0])) {
                        Log.printError("ERROR " + args[0] + "unknown name");
                        return -1;
                    }

                    varibles.remove(args[0]);
                }
                break;

                case ADD: {
                    if (stack.size() < 2) {
                        Log.printError("ERROR " + currentCommand + "stack size too small");
                        return -2;
                    }

                    StackData b = stack.pop();
                    StackData a = stack.pop();

                    StackData answ;

                    if (a.getType() == Type.INT && b.getType() == Type.INT) {
                         answ = new StackData((long)a.getData() + (long)b.getData(), Type.INT);
                    } else if ((a.getType() == Type.DOUBLE && b.getType() == Type.DOUBLE)
                            || (a.getType() == Type.DOUBLE && b.getType() == Type.INT)
                            || (a.getType() == Type.INT && b.getType() == Type.DOUBLE)) {

                        double tmpA = Double.parseDouble(a.getData().toString()), tmpB = Double.parseDouble(b.getData().toString());
                        answ = new StackData(tmpA + tmpB, Type.DOUBLE);
                    } else if (a.getType() == Type.STRING && b.getType() == Type.STRING) {
                        answ = new StackData((String)a.getData() + (String)b.getData(), Type.STRING);
                    } else {
                        Log.printError("ERROR: operator + not defined for " + a.toString() + " " + b.toString());
                        return -1;
                    }

                    stack.push(answ);
                }
                break;

                case MULT: {
                    if (stack.size() < 2) {
                        Log.printError("Error: stack size too small");
                        return -2;
                    }

                    StackData b = stack.pop();
                    StackData a = stack.pop();
                    StackData answ = new StackData();
                    if (a.getType() == Type.INT && b.getType() == Type.INT) {
                        long tmpA = Long.parseLong(a.getData().toString()), tmpB = Long.parseLong(b.getData().toString());
                        answ = new StackData(tmpA * tmpB, Type.INT);
                    }

                    stack.push(answ);
                }
                break;

                case LESSER: {
                    if (stack.size() < 2) {
                        Log.printError("ERROR " + currentCommand + "stack size too small");
                        return -2;
                    }

                    StackData b = stack.pop();
                    StackData a = stack.pop();

                    StackData answ = new StackData(null, Type.NULL);

                    if (a.getType() == Type.INT && b.getType() == Type.INT) {
                        answ = new StackData((long)a.getData() < (long)b.getData(), Type.BOOLEAN);
                    }

                    stack.push(answ);
                }
                break;

                case GREATER: {
                    if (stack.size() < 2) {
                        Log.printError("ERROR " + currentCommand + "stack size too small");
                        return -2;
                    }

                    StackData b = stack.pop();
                    StackData a = stack.pop();

                    StackData answ = new StackData(null, Type.NULL);

                    if (a.getType() == Type.INT && b.getType() == Type.INT) {
                        answ = new StackData((long)a.getData() > (long)b.getData(), Type.BOOLEAN);
                    }

                    stack.push(answ);
                }
                break;

                case CONDITION: {
                    if (stack.size() < 1) {
                        Log.printError("ERROR " + currentCommand + "stack size too small");
                        return -2;
                    }

                    StackData cond = stack.pop();
                    if (cond.getType() != Type.BOOLEAN) {
                        Log.printError("ERROR " + cond + " condition must be boolean");
                        return -1;
                    }


                    if (!(boolean)cond.getData()) {
                        commandIndex += Integer.parseInt(currentCommand.getArgs()[0]);
                        continue;
                    }
                }
                break;

                case GOTO: {
                    //System.out.println(">> " + commandIndex);
                    commandIndex += Integer.parseInt(currentCommand.getArgs()[0]);
                    continue;
                }

                case OUTPUT: {    // bad command
                    StackData tp = stack.pop();
                    System.out.print(tp.getData());
                }
                break;

            }

            commandIndex++;
        }

        return 0;
    }

    public Stack<StackData> getStack() {
        return stack;
    }

    public Map<String, Varible> getVaribles() {
        return varibles;
    }
}
