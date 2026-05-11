package cfh.jfalse.stack;

public class Value extends StackObject {

    private final int value;
    
    public Value(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
    
    @Override
    public boolean getBoolean() {
        return value != 0;
    }
    
    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
