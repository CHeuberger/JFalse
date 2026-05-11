package cfh.jfalse.cmd;

import java.io.IOException;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.stack.Value;

public class Read extends Command {

    @Override
    public void execute0(Environment environment) throws ExecutionException {
        try {
            int ch = environment.getInput().read();
            environment.getStack().push(new Value(ch));
        } catch (IOException ex) {
            throw new ExecutionException("Exception reading", ex);
        }
    }

    @Override
    public String toString() {
        return "^";
    }

}
