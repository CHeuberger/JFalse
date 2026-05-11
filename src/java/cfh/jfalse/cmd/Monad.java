package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.Stack;
import cfh.jfalse.stack.StackObject;

public abstract class Monad extends Command {

    @Override
    public final void execute0(Environment environment) throws ExecutionException, InterruptedException {
        Stack stack = environment.getStack();
        checkStackSize(stack, 1);
        StackObject obj = stack.pop();
        try {
            execute0(obj, environment);
        } catch (ExecutionException ex) {
            stack.push(obj);
            throw ex;
        }
    }
    
    protected abstract void execute0(StackObject val, Environment env) throws ExecutionException, InterruptedException;

    @Override
    public abstract String toString();
}
