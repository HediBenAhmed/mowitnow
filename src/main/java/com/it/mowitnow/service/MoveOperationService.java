package com.it.mowitnow.service;

import com.it.mowitnow.command.Command;
import com.it.mowitnow.model.LawnDimensions;
import com.it.mowitnow.model.MowerCommand;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MoveOperationService {
    public void execute(MowerCommand mowerCommand, LawnDimensions lawnDimensions) {
        Arrays.stream(mowerCommand.commands().split("")).map(Command::valueOf)
                .forEach(command -> command.getOperation().execute(mowerCommand.mower(), lawnDimensions));
    }
}
