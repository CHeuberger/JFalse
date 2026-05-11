package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.Variables;
import cfh.jfalse.stack.StackObject;
import cfh.jfalse.stack.Value;

public class Recover extends Monad {

    @Override
    public void execute0(StackObject obj, Environment env) throws ExecutionException {
        Variables var = env.getVariables();
        StackObject value = var.recover(obj.getAddress());
        if (value == null) {
            value = new Value(0);
        }
        env.getStack().push(value);
    }

    @Override
    public String toString() {
        return ";";
    }

}
