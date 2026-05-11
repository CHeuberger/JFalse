package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.stack.StackObject;

public class Drop extends Monad {

    @Override
    public void execute0(StackObject obj, Environment env) {
        // obj already removed from stack
    }
    
    @Override
    public String toString() {
        return "%";
    }
}
