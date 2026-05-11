package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.Stack;
import cfh.jfalse.stack.StackObject;

public class Swap extends Dyad {

    @Override
    protected void execute0(StackObject o1, StackObject o2, Environment env) {
        Stack stack = env.getStack();
        stack.push(o2);
        stack.push(o1);
    }

    @Override
    public String toString() {
        return "\\";
    }
}
