package cfh.jfalse;

import cfh.jfalse.stack.StackObject;

public interface Variables {

    public void assign(char address, StackObject value);
    
    public StackObject recover(char address);
}
