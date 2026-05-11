package cfh.jfalse.stack;

import cfh.jfalse.Environment;
import cfh.jfalse.ExecutionException;
import cfh.jfalse.WrongTypeException;

public abstract class StackObject {

    public int getValue() throws ExecutionException {
        throw new WrongTypeException("Value expected, got " + getName());
    }
    
    public char getAddress() throws ExecutionException {
        throw new WrongTypeException("Address expected, got " + getName());
    }
    
    public boolean getBoolean() throws ExecutionException {
        throw new WrongTypeException("Bool expected, got " + getName());
    }
    
    @SuppressWarnings("unused")
    public void execute(Environment environment) throws ExecutionException, InterruptedException {
        throw new WrongTypeException("Lambda function expected, got " + getName());
    }
    
    public String getName() {
        return getClass().getSimpleName() + ": " + toString();
    }
    
    @Override
    public abstract String toString();
}
