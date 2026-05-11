package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.NotEnoughElementsException;
import cfh.jfalse.Stack;
import cfh.jfalse.stack.StackObject;

public class Rot extends Command {

    @Override
    public void execute0(Environment environment) throws NotEnoughElementsException {
        Stack stack = environment.getStack();
        checkStackSize(stack, 3);
        StackObject o3 = stack.pop();
        StackObject o2 = stack.pop();
        StackObject o1 = stack.pop();
        stack.push(o2);
        stack.push(o3);
        stack.push(o1);
    }

    @Override
    public String toString() {
        return "@";
    }

}
