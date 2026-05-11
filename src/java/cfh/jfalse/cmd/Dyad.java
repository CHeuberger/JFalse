package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.Stack;
import cfh.jfalse.stack.StackObject;

public abstract class Dyad extends Command {

    @Override
    public final void execute0(Environment environment) throws ExecutionException, InterruptedException {
        Stack stack = environment.getStack();
        checkStackSize(stack, 2);
        StackObject o2 = stack.pop();
        StackObject o1 = stack.pop();
        try {
            execute0(o1, o2, environment);
        } catch (ExecutionException ex) {
            stack.push(o1);
            stack.push(o2);
            throw ex;
        }
    }
    
    protected abstract void execute0(StackObject o1, StackObject o2, Environment env) throws ExecutionException, InterruptedException;

    @Override
    public abstract String toString();
}
