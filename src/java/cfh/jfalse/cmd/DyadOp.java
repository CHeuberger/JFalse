package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.stack.StackObject;
import cfh.jfalse.stack.Value;

public abstract class DyadOp extends Dyad {

    @Override
    protected void execute0(StackObject o1, StackObject o2, Environment env) throws ExecutionException {
        int v1 = o1.getValue();
        int v2 = o2.getValue();
        int result = calculate(v1, v2);
        env.getStack().push(new Value(result));
    }

    protected abstract int calculate(int v1, int v2) throws ExecutionException;

    @Override
    public abstract String toString();
}
