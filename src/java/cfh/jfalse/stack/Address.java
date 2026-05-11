package cfh.jfalse.stack;

import cfh.jfalse.WrongTypeException;


public class Address extends StackObject {

    private final char address;
    
    public Address(char address) {
        if (address < 'a' || address > 'z')
            throw new IllegalArgumentException(Character.toString(address));
        this.address = address;
    }
    
    @Override
    public char getAddress() throws WrongTypeException {
        return address;
    }

    @Override
    public String toString() {
        return Character.toString(address);
    }
}
