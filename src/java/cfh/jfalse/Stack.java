package cfh.jfalse;

import cfh.jfalse.stack.StackObject;

public interface Stack {

    public void push(StackObject obj);

    public StackObject pop();
    
    public StackObject peek();
    
    public StackObject peek(int index);

    public int size();
}
