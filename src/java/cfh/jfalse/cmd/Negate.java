package cfh.jfalse.cmd;

public class Negate extends MonadOp {

    @Override
    protected int calculate(int val) {
        return -val;
    }

    @Override
    public String toString() {
        return "_";
    }

}
