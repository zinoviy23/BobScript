package com.BobScript.Parsing;

import java.util.ArrayList;
import com.BobScript.BobCode.Command;
import com.BobScript.BobCode.Commands;
import com.BobScript.BobCode.Log;

/**
 * Created by zinov on 21.02.2016.
 *
 * class for compiling to BobCode
 */
public class Parser {
    private ArrayList<Command> compiledProgram;

    public Parser() {
        compiledProgram = new ArrayList<>();
    }

    public int compile(Operand line) {
        int index = line.next();
        if (line.get(index).getPriority() == 0)
            return 0;
        Token tk = line.get(index);
        System.out.println(tk);
        if (tk.isKeyword()) {
            if (tk.getToken().equals("var")) {        // creating var
                if (line.size() <= index) {
                    Log.printError("Error: var hasn't name of variable");
                    return -1;
                }
                Token nameVarible = line.get(index + 1);
                compiledProgram.add(new Command(Commands.CREATE, nameVarible.getToken()));
                tk.setUsed();
                nameVarible.setUsed();
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
                if (!(a.getPriority() == 0))   // if a not used
                    compiledProgram.add(new Command(Commands.PUSH, a.getToken()));
                if (!(b.getPriority() == 0))  // if b not used
                    compiledProgram.add(new Command(Commands.PUSH, b.getToken()));
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
            }
            else if (tk.getToken().equals("=")) {  // assign
                if (index < 1) {
                    Log.printError("Error: nothing to assign");
                    return -1;
                }

                //  get name and compile value
                Token nameVarible = line.get(index - 1);
                tk.setUsed();
                nameVarible.setUsed();
                compile(line);
                compiledProgram.add(new Command(Commands.ASSIGN, nameVarible.getToken()));
            }
        } else if (tk.isNumber()) {   // if number without anything
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
