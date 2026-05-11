package cfh.jfalse.cmd;


public class Not extends MonadBool {

    @Override
    protected boolean calculate(boolean val) {
        return !val;
    }

    @Override
    public String toString() {
        return "~";
    }

}
