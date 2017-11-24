package com.BobScript.Parsing;

import java.util.ArrayList;
import java.util.Stack;

import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;
import com.BobScript.BobCode.Log;

/**
 * Created by zinov on 21.02.2016.
 *
 * class for compiling to BobCode
 */
@Deprecated
public class Parser {
    private ArrayList<Command> compiledProgram;
    private Stack<Mark> marks;

    public Parser() {
        compiledProgram = new ArrayList<>();
        marks = new Stack<>();
    }

    public int compile(Operand line) {
        int index = line.next();
        if (line.size() == 0 || line.get(index).getPriority() == 0)
            return 0;
        Token tk = line.get(index);
        System.out.println(tk);
        if (tk.isKeyword()) {
            if (tk.getToken().equals("if")) {
                if (index == line.size() - 1) {
                    Log.printError("ERROR: no condition");
                    return -1;
                }
                tk.setUsed();
                compile(line);
                compiledProgram.add(new Command(Commands.CONDITION, ""));
                marks.push(new Mark(Mark.MarkValue.IF, 0));
            }
            else if (tk.getToken().equals("while")) {
                if (index == line.size() - 1) {
                    Log.printError("Error: no condition");
                    return -1;
                }
                tk.setUsed();
                int pos = compiledProgram.size();
                marks.push(new Mark(Mark.MarkValue.WHILE, pos));
                compile(line);
                compiledProgram.add(new Command(Commands.CONDITION, ""));
            } else if (tk.getToken().equals("function")) {
                if (index == line.size() - 1) {
                    Log.printError("Error: no condition");
                    return -1;
                }
                Token name = line.get(index + 1);
                tk.setUsed();
                name.setUsed();
                compiledProgram.add(new Command(Commands.FUNCTION, name.getToken()));
                if (!line.get(index + 2).getToken().equals("(")) {
                    Log.printError("Error: lost ( in function definition");
                    return -1;
                }

                int kek = line.getCloseParenthesis(index + 2);
                line.get(index + 2).setUsed();
                line.get(kek).setUsed();
                ArrayList<String> args = new ArrayList<>();
                for (int i = index + 3; i < kek; i++) {
                    if (!line.get(i).getToken().equals(",")) {
                        args.add(line.get(i).getToken());
                    }
                    line.get(i).setUsed();
                }
                compiledProgram.add(new Command(Commands.ARG_COUNT, Integer.toString(args.size())));
                for (String arg : args) {
                    compiledProgram.add(new Command(Commands.ARGUMENT, arg));
                }
                marks.push(new Mark(Mark.MarkValue.FUNCTION, 0));
                compile(line);

            } else if (tk.getToken().equals("end")) {
                if (marks.size() == 0) {
                    Log.printError("Error: nothing to end");
                    return -1;
                }

                tk.setUsed();
                if (marks.peek().getMark() == Mark.MarkValue.IF) {
                    marks.pop();
                    compiledProgram.add(new Command(Commands.END_CONDITION, ""));
                } else if (marks.peek().getMark() == Mark.MarkValue.WHILE) {
                    compiledProgram.add(new Command(Commands.GOTO,
                            Integer.toString(-compiledProgram.size() + marks.peek().getPosition())));
                    marks.pop();
                    compiledProgram.add(new Command(Commands.END_CONDITION, ""));
                } else if (marks.peek().getMark() == Mark.MarkValue.FUNCTION) {
                    compiledProgram.add(new Command(Commands.END_FUNCTION, ""));
                    marks.pop();
                }
                compile(line);
            }
        }
        if (tk.isDelimiter()) {
            if (tk.getToken().equals("+")) {     // add
                if (index < 1 || index == line.size() - 1) {
                    Log.printError("Error: nothing to add");
                    return -1;
                }

                Token a = line.get(index - 1);
                Token b = line.get(index + 1);
                if (a.getPriority() != 0) {   // if a not used
                    compiledProgram.add(new Command(Commands.PUSH, a.getToken()));
                }

                if (b.getPriority() != 0) {  // if b not used
                    if (line.size() > index + 2 && line.get(index + 2).getToken().equals("(")) {
                        Operand somthing = line.extractFromParenthesis(index + 2, line.getCloseParenthesis(index + 2));
                        Operand[] arguments = somthing.split(",");
                        //System.out.println("BOB: " +somthing);
                        for (Operand kek : arguments) {
                            //compile(kek);
                            System.out.println("BOB: " + kek);
                        }
                        compiledProgram.add(new Command(Commands.CALL, b.getToken()));
                    } else
                        compiledProgram.add(new Command(Commands.PUSH, b.getToken()));
                }
                compiledProgram.add(new Command(Commands.ADD, ""));
                tk.setUsed();
                a.setUsed();
                b.setUsed();
                compile(line);
            } else if (tk.getToken().equals("*")) {   // multiply
                if (index < 1 || index == line.size() - 1) {
                    Log.printError("Error: nothing to multiply");
                    return -1;
                }
                Token a = line.get(index - 1);
                Token b = line.get(index + 1);
                if (!(a.getPriority() == 0))  // if a not used
                    compiledProgram.add(new Command(Commands.PUSH, a.getToken()));
                if (!(b.getPriority() == 0))  // if b not used
                    compiledProgram.add(new Command(Commands.PUSH, b.getToken()));
                compiledProgram.add(new Command(Commands.MULT, ""));
                tk.setUsed();
                a.setUsed();
                b.setUsed();
                compile(line);
            } else if (tk.getToken().equals("=")) {  // assign
                if (index < 1) {
                    Log.printError("Error: nothing to assign");
                    return -1;
                }

                //  get name and compile value
                Token nameVariable = line.get(index - 1);
                tk.setUsed();
                nameVariable.setUsed();
                compile(line);
                compiledProgram.add(new Command(Commands.ASSIGN, nameVariable.getToken()));
            } else if (tk.getToken().equals("<")) {
                if (index < 1 || index == line.size() - 1) {
                    Log.printError("Error: nothing operand <");
                    return -1;
                }

                Token a = line.get(index - 1);
                Token b = line.get(index + 1);
                if (a.getPriority() != 0)
                    compiledProgram.add(new Command(Commands.PUSH, a.getToken()));
                if (b.getPriority() != 0)
                    compiledProgram.add(new Command(Commands.PUSH, b.getToken()));
                compiledProgram.add(new Command(Commands.LESSER, ""));
                tk.setUsed();
                a.setUsed();
                b.setUsed();
                compile(line);
            } else if (tk.getToken().equals(">")) {
                if (index < 1 || index == line.size() - 1) {
                    Log.printError("Error: nothing to operand >");
                    return -1;
                }

                Token a = line.get(index - 1);
                Token b = line.get(index + 1);
                if (a.getPriority() != 0)
                    compiledProgram.add(new Command(Commands.PUSH, a.getToken()));
                if (b.getPriority() != 0)
                    compiledProgram.add(new Command(Commands.PUSH, b.getToken()));
                compiledProgram.add(new Command(Commands.GREATER, ""));
                tk.setUsed();
                a.setUsed();
                b.setUsed();
                compile(line);
            } else if (tk.getToken().equals("==")) {
                if (index < 1 || index == line.size() - 1) {
                    Log.printError("Error: nothing to operand ==");
                    return -1;
                }
                Token a = line.get(index - 1);
                Token b = line.get(index + 1);

                if (a.getPriority() != 0)
                    compiledProgram.add(new Command(Commands.PUSH, a.getToken()));
                if (b.getPriority() != 0)
                    compiledProgram.add(new Command(Commands.PUSH, b.getToken()));
                compiledProgram.add(new Command(Commands.EQUAL, ""));
                tk.setUsed();
                a.setUsed();
                b.setUsed();
                compile(line);
            }

        } else {
            // здесь должна быть оброботка того, что можно поставить тупо 5
            // должно чекаться, что уже что-то проверяется, какая-то операция, и только потом добавляться
            // иначе чекаться, что это функция, если не функция, то еррор
            if (tk.getPriority() == 0)
                return 0;
            compiledProgram.add(new Command(Commands.PUSH, tk.getToken()));
            tk.setUsed();
            compile(line);
        }
        return 0;
    }

    // get compiled program
    public Command[] getProgram() {
        Command[] answ = new Command[compiledProgram.size()];
        for (int i = 0; i < answ.length; i++)
            answ[i] = compiledProgram.get(i);

        return answ;
    }

}
