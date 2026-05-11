package cfh.jfalse;

import java.util.Arrays;

import cfh.jfalse.stack.StackObject;

public class SingleVariables implements Variables {

    protected final StackObject[] memory;

    public SingleVariables() {
        memory = new StackObject[index('z') + 1];
    }
    
    @Override
    public void assign(char address, StackObject value) {
        int index = index(address);
        memory[index] = value;
    }

    @Override
    public StackObject recover(char address) {
        return memory[index(address)];
    }

    protected int index(char address) {
        return address - 'a';
    }
    
    @Override
    public String toString() {
        return Arrays.toString(memory);
    }
}
