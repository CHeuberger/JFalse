package cfh.jfalse.stack;

import cfh.jfalse.ExecutionException;

public class Bool extends StackObject {

    public static final Bool FALSE = new Bool(false);
    public static final Bool TRUE = new Bool(true);
    
    public static Bool valueOf(boolean value) {
        if (value)
            return TRUE;
        else
            return FALSE;
    }
    
    //----------------------------------------------------------------------------------------------
    
    private boolean value;
    
    private Bool(boolean value) {
        this.value = value;
    }
    
    @Override
    public int getValue() throws ExecutionException {
        return value ? -1 : 0;
    }
    
    @Override
    public boolean getBoolean() throws ExecutionException {
        return value;
    }
    
    @Override
    public String toString() {
        return Boolean.toString(value);
    }

}
