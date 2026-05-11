package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.Variables;
import cfh.jfalse.stack.StackObject;

public class Assign extends Dyad {

    @Override
    public void execute0(StackObject o1, StackObject o2, Environment env) throws ExecutionException {
        Variables var = env.getVariables();
        var.assign(o2.getAddress(), o1);
    }

    @Override
    public String toString() {
        return ":";
    }

}
