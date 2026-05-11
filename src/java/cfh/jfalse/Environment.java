package cfh.jfalse;

import static cfh.jfalse.Environment.Status.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Environment {
    
    public static final String PROP_STATUS = "status";
    
    private final Input input;
    private final Output output;
    private final Stack stack;
    private final Variables variables;
    
    private Status status = IDLE;
    private int level = 0;
    
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    
    public Environment(Input input, Output output, Stack stack, Variables variables) {
        this.input = input;
        this.output = output;
        this.stack = stack;
        this.variables = variables;
    }
    
    public Stack getStack() {
        return stack;
    }
    
    public Variables getVariables() {
        return variables;
    }

    public Input getInput() {
        return input;
    }
    
    public Output getOutput() {
        return output;
    }

    public Status setStatus(Status status) {
        if (status == null) throw new NullPointerException();
        Status old = this.status;
        if (status != old) {
            this.status = status;
            changeSupport.firePropertyChange(PROP_STATUS, old, status);
        }
        return old;
    }
    
    public void resetLevel() {
        level = 0;
    }
    
    public void incLevel() {
        level += 1;
    }
    
    public void decLevel() {
        level -= 1;
    }
    
    public int getLevel() {
        return level;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(propertyName, listener);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    public enum Status {
        IDLE,
        RUNNING,
        PAUSE;
    }
}
