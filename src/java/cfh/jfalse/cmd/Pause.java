package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.Environment.Status;
import cfh.jfalse.stack.StackObject;

public class Pause extends Monad {

    @Override
    protected void execute0(StackObject val, Environment env) throws ExecutionException, InterruptedException {
        int millis = val.getValue();
        Status old = env.setStatus(Status.PAUSE);
        try {
            Thread.sleep(millis);
        } finally {
            env.setStatus(old);
        }
    }

    @Override
    public String toString() {
        return "µ";
    }
}
