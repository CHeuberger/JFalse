package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.NotEnoughElementsException;
import cfh.jfalse.Stack;

public class Dup extends Command {

    @Override
    public void execute0(Environment environment) throws NotEnoughElementsException {
        Stack stack = environment.getStack();
        checkStackSize(stack, 1);
        stack.push(stack.peek());
    }
    
    @Override
    public String toString() {
        return "$";
    }
}
