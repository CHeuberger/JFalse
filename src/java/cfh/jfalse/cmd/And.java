package cfh.jfalse.cmd;

import cfh.jfalse.ExecutionException;

public class And extends DyadBool {
    
    @Override
    protected boolean calculate(boolean v1, boolean v2) throws ExecutionException {
        return v1 & v2;
    }

    @Override
    public String toString() {
        return "&";
    }
}
