package cfh.jfalse.cmd;

import cfh.jfalse.Environment;
import cfh.jfalse.stack.Value;

public class Char extends Command {

    private final char value;
    
    public Char(char value) {
        this.value = value;
    }
    
    @Override
    public void execute0(Environment environment) {
        environment.getStack().push(new Value(value));
    }
    
    @Override
    public String toString() {
        return "'" + value;
    }
}
