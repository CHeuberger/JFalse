package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.stack.StackObject;

public class If extends Dyad {

    @Override
    protected void execute0(StackObject o1, StackObject o2, Environment env) throws ExecutionException, InterruptedException {
        boolean test = o1.getBoolean();
        if (test) {
            fireTrace("?\u21D2  " + o2, env);
            o2.execute(env);
        }
    }

    @Override
    public String toString() {
        return "?";
    }
}
