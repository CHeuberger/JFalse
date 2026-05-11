package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.stack.Bool;
import cfh.jfalse.stack.StackObject;

public abstract class DyadBool extends Dyad {

    @Override
    protected void execute0(StackObject o1, StackObject o2, Environment env) throws ExecutionException {
        boolean v1 = o1.getBoolean();
        boolean v2 = o2.getBoolean();
        boolean result = calculate(v1, v2);
        env.getStack().push(Bool.valueOf(result));
    }

    protected abstract boolean calculate(boolean v1, boolean v2) throws ExecutionException;

    @Override
    public abstract String toString();
}
