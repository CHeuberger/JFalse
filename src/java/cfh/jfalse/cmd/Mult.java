package cfh.jfalse.cmd;

public class Mult extends DyadOp {

    @Override
    protected int calculate(int v1, int v2) {
        return v1 * v2;
    }

    @Override
    public String toString() {
        return "*";
    }
}
