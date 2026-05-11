package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.stack.StackObject;

public class Apply extends Monad {

    @Override
    public void execute0(StackObject obj, Environment env) throws ExecutionException, InterruptedException {
        obj.execute(env);
    }
    
    @Override
    public String toString() {
        return "!";
    }
}
