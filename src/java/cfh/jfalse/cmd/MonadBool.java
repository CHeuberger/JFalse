package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.stack.Bool;
import cfh.jfalse.stack.StackObject;

public abstract class MonadBool extends Monad {

    @Override
    protected void execute0(StackObject obj, Environment env) throws ExecutionException {
        boolean val = obj.getBoolean();
        boolean result = calculate(val);
        env.getStack().push(Bool.valueOf(result));
    }

    protected abstract boolean calculate(boolean val) throws ExecutionException;

    @Override
    public abstract String toString();
}
