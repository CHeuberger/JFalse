package cfh.jfalse;

import java.io.PrintStream;

public class PrintOutput implements Output {

    private final PrintStream stream;
    
    public PrintOutput(PrintStream stream) {
        if (stream == null) throw new NullPointerException();
        
        this.stream = stream;
    }
    
    @Override
    public void append(char ch) {
        stream.print(ch);
    }

    @Override
    public void append(int value) {
        stream.print(value);
    }

    @Override
    public void append(String text) {
        stream.print(text);
    }

    @Override
    public void flush() {
        stream.flush();
    }

}
