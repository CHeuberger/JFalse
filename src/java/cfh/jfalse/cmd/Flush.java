package cfh.jfalse.cmd;

import java.io.IOException;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;

public class Flush extends Command {

    @Override
    public void execute0(Environment environment) throws ExecutionException {
        try {
            environment.getInput().flush();
        } catch (IOException ex) {
            throw new ExecutionException("Exception flushing input", ex);
        }
        environment.getOutput().flush();
    }

    @Override
    public String toString() {
        return "ß";
    }
}
