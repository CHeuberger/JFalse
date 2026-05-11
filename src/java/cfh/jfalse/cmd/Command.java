package cfh.jfalse.cmd;

import java.util.ArrayList;
import java.util.List;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.NotEnoughElementsException;
import cfh.jfalse.Stack;

public abstract class Command {

    private static final List<Listener> listeners = new ArrayList<Listener>();
    
    public static void addListener(Listener listener) {
        listeners.add(listener);
    }
    
    public static void removeListener(Listener listener) {
        listeners.remove(listener);
    }
    
    protected static void fireBeforeExecute(Command command, Environment environment) {
        for (Listener listener : listeners) {
            listener.beforeExecute(command, environment);
        }
    }
    
    protected static void fireTrace(String message, Environment environment) {
        for (Listener listener : listeners) {
            listener.trace(message, environment);
        }
    }
    
    //==============================================================================================
    
    public void execute(Environment environment) throws ExecutionException, InterruptedException {
        fireBeforeExecute(this, environment);
        execute0(environment);
    }
    
    protected void checkStackSize(Stack stack, int required) throws NotEnoughElementsException {
        if (stack.size() < required)
            throw new NotEnoughElementsException("required " + required + " element(s) on stack");
    }
    
    protected abstract void execute0(Environment environment) throws ExecutionException, InterruptedException;
    
    @Override
    public abstract String toString();
    
    //==============================================================================================
    
    public interface Listener {
        
        public void beforeExecute(Command command, Environment environment);
        
        public void trace(String message, Environment environment);
        
    }
}
