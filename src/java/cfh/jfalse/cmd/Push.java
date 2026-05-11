package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.stack.StackObject;

public class Push extends Command {

    private final StackObject object;
    
    public Push(StackObject object) {
        this.object = object;
    }
    
    @Override
    public void execute0(Environment environment) {
        environment.getStack().push(object);
    }
    
    @Override
    public String toString() {
        return object.toString();
    }
}
