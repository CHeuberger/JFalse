package cfh.jfalse;

public interface Output {

    public void append(char ch);
    
    public void append(int value);

    public void append(String text);
    
    public void flush();
}
