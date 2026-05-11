package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.stack.StackObject;

public class Write extends Monad {

    @Override
    protected void execute0(StackObject val, Environment env) throws ExecutionException {
        char ch = (char) val.getValue();
        env.getOutput().append(ch);
    }

    @Override
    public String toString() {
        return ",";
    }
}
