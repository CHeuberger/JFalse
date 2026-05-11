package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.Stack;
import cfh.jfalse.WrongTypeException;
import cfh.jfalse.stack.Lambda;
import cfh.jfalse.stack.StackObject;

public class While extends Dyad {

    @Override
    protected void execute0(StackObject o1, StackObject o2, Environment env) throws ExecutionException, InterruptedException {
        if (!(o2 instanceof Lambda))
            throw new WrongTypeException("Lambda function expected, got " + o2.getName());
        while (condition(o1, env)) {
            fireTrace("#\u21D2  " + o2, env);
            o2.execute(env);
        }
    }
    
    private boolean condition(StackObject condition, Environment env) throws ExecutionException, InterruptedException {
        fireTrace("#?  " + condition, env);
        condition.execute(env);
        fireTrace("#!  " + condition, env);
        Stack stack = env.getStack();
        checkStackSize(stack, 1);
        return stack.pop().getBoolean();
    }

    @Override
    public String toString() {
        return "#";
    }
}
