package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.stack.Bool;
import cfh.jfalse.stack.StackObject;

public class Greater extends Dyad {

    @Override
    protected void execute0(StackObject o1, StackObject o2, Environment env) throws ExecutionException {
        int v1 = o1.getValue();
        int v2 = o2.getValue();
        boolean result = (v1 > v2);
        env.getStack().push(Bool.valueOf(result));
    }

    @Override
    public String toString() {
        return ">";
    }

}
