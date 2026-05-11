package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.stack.StackObject;

public class PrintNumber extends Monad {

    @Override
    protected void execute0(StackObject obj, Environment env) throws ExecutionException {
        int value = obj.getValue();
        env.getOutput().append(value);
    }

    @Override
    public String toString() {
        return ".";
    }

}
