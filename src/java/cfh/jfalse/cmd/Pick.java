package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.Stack;
import cfh.jfalse.stack.StackObject;

public class Pick extends Monad {

    @Override
    protected void execute0(StackObject val, Environment env) throws ExecutionException {
        Stack stack = env.getStack();
        int index = val.getValue();
        stack.push(stack.peek(index));
    }

    @Override
    public String toString() {
        return "ø";
    }

}
