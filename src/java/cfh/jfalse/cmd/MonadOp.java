package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.stack.StackObject;
import cfh.jfalse.stack.Value;

public abstract class MonadOp extends Monad {

    @Override
    protected void execute0(StackObject obj, Environment env) throws ExecutionException {
        int val = obj.getValue();
        int result = calculate(val);
        env.getStack().push(new Value(result));
    }

    protected abstract int calculate(int val) throws ExecutionException;

    @Override
    public abstract String toString();
}
