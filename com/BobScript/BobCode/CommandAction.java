package com.BobScript.BobCode;

/**
 * Created by Alexandr on 19.09.2017. <br>
 * Interface for command actions
 */

public interface CommandAction {
    CommandResult Action(Command currentCommand);
}
