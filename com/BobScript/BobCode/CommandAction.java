package com.BobScript.BobCode;

/**
 * Created by Alexandr on 19.09.2017.
 *
 *
 * Интерфейс для обобщения команд
 */

public interface CommandAction {
    int Action(String[] args, Command currentCommand);
}
