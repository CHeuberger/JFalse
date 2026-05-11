package cfh.jfalse.stack;

import java.util.ArrayList;
import java.util.List;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.Environment.Status;
import cfh.jfalse.cmd.Command;

public class Lambda extends StackObject {

    private final List<Command> commands;
    
    public Lambda() {
        commands = new ArrayList<Command>();
    }
    
    public void append(Command command) {
        commands.add(command);
    }
    
    @Override
    public void execute(Environment environment) throws ExecutionException, InterruptedException {
        Status old = environment.setStatus(Status.RUNNING);
        environment.incLevel();
        try {
            for (Command cmd : commands) {
                if (Thread.interrupted())
                    throw new InterruptedException();
                cmd.execute(environment);
            }
        } finally {
            environment.decLevel();
            environment.setStatus(old);
        }
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        for (Command cmd : commands) {
            builder.append(cmd);
        }
        builder.append("]");
        return builder.toString();
    }
}
