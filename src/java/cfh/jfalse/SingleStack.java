package cfh.jfalse;

import java.util.LinkedList;

import cfh.jfalse.stack.StackObject;

public class SingleStack implements Stack {

    private final LinkedList<StackObject> data;
    
    public SingleStack() {
        data = new LinkedList<StackObject>();
    }
    
    @Override
    public void push(StackObject obj) {
        synchronized (data) {
            data.addLast(obj);
        }
    }
    
    @Override
    public StackObject pop() {
        synchronized (data) {
            return data.removeLast();
        }
    }
    
    @Override
    public StackObject peek() {
        synchronized (data) {
            return data.peekLast();
        }
    }
    
    @Override
    public StackObject peek(int index) {
        synchronized (data) {
            return data.get(data.size() - index - 1);
        }
    }

    @Override
    public int size() {
        synchronized (data) {
            return data.size();
        }
    }
    
    @Override
    public String toString() {
        return data.toString();
    }
}
