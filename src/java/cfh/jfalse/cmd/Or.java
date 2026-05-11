package cfh.jfalse.cmd;


public class Or extends DyadBool {

    @Override
    protected boolean calculate(boolean v1, boolean v2) {
        return v1 | v2;
    }

    @Override
    public String toString() {
        return "|";
    }

}
